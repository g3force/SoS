/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.influence.EInfluenceType;
import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.course.io.CourseSaver;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.observers.Observers;
import edu.dhbw.sos.simulation.SimController;


/**
 * This class is a data holder for the data belonging to the course
 * It also provides simulation methods
 * 
 * @author DirkK
 */
public class Course {
	private static final Logger									logger	= Logger.getLogger(Course.class);
	
	// statistics (data for each simulation step)
	private transient LinkedList<Stats>							statistics;
	// current statistics (average parameter values of students)
	private transient CalcVector									statAvgStudentState;
	private transient LinkedHashMap<Integer, CalcVector>	histStatAvgStudentStates;
	
	// persistent data
	private Lecture													lecture;
	private IPlace[][]												students;
	private Influence													influence;
	private String														name;
	private LinkedList<String>										parameters;
	
	// the student and property that was selected in the GUI (by hovering over the student)
	private transient IPlace										selectedStudent;
	private transient int											selectedProperty;
	// simulating indicates, that we are currently within a simulation step (important for donInput)
	private transient boolean										simulating;
	private transient LinkedList<DonInput>						donInputQueue;


	public enum ECourseType {
		THEORY,
		GROUP,
		HOLIDAY,
		NORMAL
	}
	
	
	public Course(String name) {
		createDummyCourse(name, ECourseType.NORMAL);
	}
	

	public Course(String name, ECourseType type) {
		createDummyCourse(name, type);
	}
	
	
	private void createDummyCourse(String name, ECourseType type) {
		this.name = name;
		
		int rows = (int) (Math.random() * 5) + 5;
		int columns = (int) (Math.random() * 5) + 10;
		students = new IPlace[rows][columns];
		parameters = new LinkedList<String>();
		parameters.add("Awakeness");
		parameters.add("Silence");
		parameters.add("Attention");
		parameters.add("Quality");
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				double createStudent = Math.random();
				if (createStudent > 0.8 && (x != 0 || y != 0)) {
					students[y][x] = new EmptyPlace(parameters.size());
				} else {
					Student newStud = new Student(parameters.size());
					
					for (int i = 0; i < newStud.getActualState().size(); i++) {
						newStud.addToChangeVector(i, (float) ((Math.random() * 100) - 50));
						newStud.addValueToStateVector(i, (int) (Math.random() * 10));
					}
					students[y][x] = newStud;
				}
			}
		}
		influence = new Influence();
		TimeBlocks tbs = new TimeBlocks();
		switch (type) {
			case HOLIDAY:
				tbs.add(new TimeBlock(20, BlockType.pause));
				tbs.add(new TimeBlock(5, BlockType.theory));
				tbs.add(new TimeBlock(30, BlockType.exercise));
				tbs.add(new TimeBlock(5, BlockType.theory));
				tbs.add(new TimeBlock(10, BlockType.pause));
				tbs.add(new TimeBlock(5, BlockType.theory));
				break;
			case GROUP:
				tbs.add(new TimeBlock(20, BlockType.theory));
				tbs.add(new TimeBlock(5, BlockType.pause));
				tbs.add(new TimeBlock(80, BlockType.group));
				break;
			case THEORY:
				tbs.add(new TimeBlock(40, BlockType.theory));
				tbs.add(new TimeBlock(5, BlockType.pause));
				tbs.add(new TimeBlock(60, BlockType.theory));
				tbs.add(new TimeBlock(10, BlockType.pause));
				tbs.add(new TimeBlock(50, BlockType.theory));
				break;
			case NORMAL:
				tbs.add(new TimeBlock(20, BlockType.theory));
				tbs.add(new TimeBlock(15, BlockType.pause));
				tbs.add(new TimeBlock(30, BlockType.exercise));
				tbs.add(new TimeBlock(30, BlockType.group));
				tbs.add(new TimeBlock(10, BlockType.pause));
				tbs.add(new TimeBlock(20, BlockType.theory));
				break;
		}
		lecture = new Lecture(new Date(), tbs);
		init();
	}
	
	
	private void init() {
		statistics = new LinkedList<Stats>();
		statAvgStudentState = new CalcVector(parameters.size());
		histStatAvgStudentStates = new LinkedHashMap<Integer, CalcVector>();
		
		selectedStudent = null;
		selectedProperty = 0;
		simulating = false;
		donInputQueue = new LinkedList<DonInput>();
		
		// calculate state statistics for whole course
		calcStatistics(0);
		Observers.notifyStatistics();
	}
	
	
	/**
	 * Reset the non persistent data (simulation, statistics, etc.)
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void reset() {
		init();
		for (int y = 0; y < students.length; y++) {
			for (int x = 0; x < students[y].length; x++) {
				students[y][x].reset();
			}
		}
	}
	
	
	/**
	 * This method is used for deserialization of the course XML file
	 * 
	 * @return course object read from serialized file
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	private Object readResolve() {
		init();
		return this;
	}
	
	
	/**
	 * 
	 * Gets a certain student-instance based on x- and y-coordinates
	 * 
	 * @param row
	 * @param column
	 * @return
	 * @author SebastianN
	 */
	public IPlace getPlace(int row, int column) {
		IPlace[][] students = this.getStudents();
		if (students != null) {
			return students[row][column];
		}
		throw new IllegalStateException();
	}
	
	
	/**
	 * 
	 * Assigns a student-instance to a place (based on row/column)
	 * 
	 * @param row
	 * @param column
	 * @param student
	 * @return TRUE if assignment was successful, otherwise FALSE
	 * @author SebastianN
	 */
	public boolean setPlace(int row, int column, IPlace student) {
		IPlace[][] students = this.getStudents();
		if (students != null) {
			students[row][column] = student;
			return true;
		}
		throw new IllegalStateException();
	}
	
	
	/**
	 * 
	 * @brief Checks whether a place is empty of not - based on x,y
	 * 
	 * @param row
	 * @param column
	 * @return TRUE if student is an instance of "EmptyPlace", otherwise FALSE.
	 * @author SebastianN
	 */
	public boolean isPlaceEmpty(int row, int column) {
		IPlace[][] students = this.getStudents();
		if (students != null) {
			if (students[row][column] instanceof EmptyPlace) {
				return true;
			}
			return false;
		}
		throw new IllegalStateException();
	}
	
	
	/**
	 * is called when the user makes an input, it queues the input first, so it cannot get lost during a simulation step
	 * 
	 * @param index which value is changed
	 * @param value how much
	 * @author dirk
	 */
	public void donInputQueue(int index, float value) {
		if (simulating) {
			logger.debug("donInput queued with index " + index + " and value " + value);
			donInputQueue.add(new DonInput(index, value));
		} else {
			donInput(index, value);
		}
	}
	
	
	/**
	 * the input of a suggestion
	 * 
	 * @param calcVec
	 * @author dirk
	 */
	public void suggestionInput(CalcVector calcVec) {
		logger.info("Suggestion performed");
		for (int y = 0; y < students.length; y++) {
			for (int x = 0; x < students[y].length; x++) {
				students[y][x].addToStateVector(calcVec);
			}
		}
	}
	
	
	/**
	 * used by the queue to input suggestions to the selected student
	 * 
	 * @param index
	 * @param value
	 * @author dirk
	 */
	public void donInput(int index, float value) {
		selectedStudent.getActualState().printCalcVector("Don Input: preActualState: ");
		selectedStudent.donInput(index, value);
		selectedStudent.getActualState().printCalcVector("Don Input: postActualState: ");
		logger.debug("donInput executed with index " + index + " and value " + value);
	}
	
	
	/**
	 * calculates the next step of the simulation
	 * calculate for every student the next state
	 * 
	 * @param currentTime current time of the lecture
	 * @author dirk
	 */
	public void simulationStep(int currentTime) {
		synchronized (this) {
			simulating = true;
			
			// calculate state statistics for whole course
			calcStatistics(currentTime);
			
			// -------------------------------------------------
			// -------------- pre conditions -------------------
			// -------------------------------------------------
			
			// the new array for the calculated students, fill it with EmptyPalce
			CalcVector[][] oldVec = new CalcVector[students.length][students[0].length];
			for (int y = 0; y < students.length; y++) {
				for (int x = 0; x < students[0].length; x++) {
					if (students[y][x] instanceof Student)
						oldVec[y][x] = ((Student) students[y][x]).getActualState().clone();
				}
			}
			
			// -------------------------------------------------
			// -------- student independent calculations -------
			// -------------------------------------------------
			
			CalcVector preChangeVector = new CalcVector(parameters.size());
			preChangeVector.printCalcVector("Sim: Init");
			
			// time block depending ( inf(Break) * breakInf )
			double timeBlockInf = 0.0004;
			
			BlockType bt = lecture.getTimeBlocks().getTimeBlockAtTime(currentTime / 60000).getType();
			preChangeVector.addCalcVector(influence.getEnvironmentVector(bt.getEInfluenceType(), timeBlockInf));
			// System.out.println(influence.getEnvironmentVector(bt.getEInfluenceType(), timeBlockInf).toString());
			// TimeBlock actual = lecture.getTimeBlocks().getTimeBlockAtTime(currentTime / 60000);
			// logger.info("currentTime: " + currentTime + " Block: " + actual.getType().toString() + " len: "
			// + actual.getLen());
			preChangeVector.printCalcVector("Sim: after timeblock (" + bt.toString() + ")");
			
			// timeDending ( inf(Time) * currentTime/1000 * timeInf )
			double timeInf = 0.00000005;
			double timeTimeInf = timeInf * currentTime / 1000; // in seconds
			preChangeVector.addCalcVector(influence.getEnvironmentVector(EInfluenceType.TIME_DEPENDING, timeTimeInf));
			preChangeVector.printCalcVector("Sim: after time depending");
			
			// -------------------------------------------------
			// ---------- iterate over all students ------------
			// -------------------------------------------------
			for (int y = 0; y < students.length; y++) {
				for (int x = 0; x < students[y].length; x++) {
					if (students[y][x] instanceof Student) {
						Student student = (Student) students[y][x];
						
						// influence of the surrounding students
						CalcVector neighborInfl = getNeighborsInfluence(student, oldVec, x, y);

						// create a new vector which contains the pre calculates vector and the neighbor vector
						CalcVector preChangeVectorSpecial = neighborInfl.clone().addCalcVector(preChangeVector);

						// create a new student and let him calculate a new change vector
						student.calcNextSimulationStep(preChangeVectorSpecial, influence, currentTime);
					}
				}
			}
			
			
			// -------------------------------------------------
			// -------------- post simulation ------------------
			// -------------------------------------------------
			
			// handle any donInputs, that had accord during simulation
			simulating = false;
			for (DonInput di : donInputQueue) {
				donInput(di.index, di.value);
			}
			donInputQueue.clear();
		}
	}
	
	
	/**
	 * calculates the changeVector concerning the neighbors for one student
	 * @param student
	 * @param x position of the student
	 * @param y position of the student
	 * @return changeVector
	 * @author dirk
	 */
	private CalcVector getNeighborsInfluence(Student student, CalcVector[][] oldVec, int x, int y) {
		
		// add all the vectors of the students around and count them
		CalcVector changeVector = new CalcVector(student.getActualState().size());
		CalcVector surronding = new CalcVector(student.getActualState().size());
		int neighbourAmount = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					int newx = x + i;
					int newy = y + j;
					if (newx < students[0].length && newx >= 0 && newy < students.length && newy >= 0) {
						if (students[newy][newx] instanceof Student) {
							surronding = surronding.addCalcVector(oldVec[newy][newx]);
							neighbourAmount++;
						}
					}
				}
			}
		}
		
		// Game of life
		// 1. calculate average of surrounding students
		// 2. calculate difference of students value and average
		// 3. changeVector = difference of students value and average
		for (int i = 0; i < changeVector.size(); i++) {
			float studentsValue = student.getActualState().getValueAt(i);
			float average = surronding.getValueAt(i) / neighbourAmount;
			
			float studentMAverage = average - studentsValue;
			if (studentMAverage < 0)
				studentMAverage *= -1;
			float reducer = (100 - studentMAverage) / 100;
			if (x == 1 && y == 1)
				logger.debug("Sim(1,1): average: " + average + " / actualState: " + studentsValue + " / reducer: "
						+ reducer + " / value: " + studentMAverage * reducer * 0.05f);
			changeVector.setValueAt(i, (average - studentsValue) * reducer * 0.01f);
			// changeVector.multiplyWithVector(influence.getEnvironmentVector(EInfluenceType.NEIGHBOR,0.01));
			// in every 3 million cases this calculation result in NaN
			if (Double.isNaN(changeVector.getValueAt(0)) || Double.isNaN(changeVector.getValueAt(1))
					|| Double.isNaN(changeVector.getValueAt(2)) || Double.isNaN(changeVector.getValueAt(3)))
				changeVector = new CalcVector(4);
		}
		for (int i = 0; i < changeVector.size(); i++) {
			float addValue = changeVector.getValueAt(i);
			if (addValue > 0) {
				changeVector.setValueAt(i, addValue * student.getChangeVector().getValueAt(i));
			} else {
				changeVector.setValueAt(i, addValue * (2 - student.getChangeVector().getValueAt(i)));
			}
			// System.out.println(addValue + "->" + addVector.getValueAt(i) + "(" + this.getChangeVector().getValueAt(i)
			// + ")");
		}
		// System.out.println(changeVector.toString() + " - " + student.getChangeVector().toString());
		return changeVector;
	}
	
	
	/**
	 * print statistical information concerning the field
	 * @author dirk
	 */
	public void printFieldInformation() {
		int isnull = 0;
		int studentsA = 0;
		int empty = 0;
		for (int a = 0; a < students.length; a++) {
			for (int b = 0; b < students[0].length; b++) {
				if (students[b][a] == null) {
					isnull++;
					System.out.println(a + " / " + b);
				} else if (students[b][a] instanceof EmptyPlace)
					empty++;
				else if (students[b][a] instanceof Student)
					studentsA++;
			}
		}
		System.out.println("null: " + isnull);
		System.out.println("empty: " + empty);
		System.out.println("studentsA: " + studentsA);
	}
	
	
	/**
	 * 
	 * Simulate until a specific time.
	 * 
	 * @param curTime time in milliseconds
	 * @param requiredTime time in milliseconds
	 * @author andres
	 */
	private void simulateUntil(int curTime, int requiredTime) {
		Observers.notifySimUntil(true);
		while (curTime < requiredTime) {
			simulationStep(curTime);
			curTime += SimController.realInterval;
		}
		// calculate state statistics for whole course
		calcStatistics(requiredTime);
		Observers.notifyStudents();
		Observers.notifySimUntil(false);
		Observers.notifyStatistics();

		logger.warn("historyStates: " + students[0][0].getHistoryStates().size());
	}
	
	
	/**
	 * set the time of the simulation to a given time
	 * the old time is needed to because the course does not know the time
	 * if the new time is in the past all history states
	 * 
	 * @param actualTime current time (milliseconds)
	 * @param time new time (milliseconds)
	 * @author dirk
	 */
	public void setTime(int actualTime, int time) {
		for (int y = 0; y < students.length; y++) {
			for (int x = 0; x < students[y].length; x++) {
				if (students[y][x] instanceof Student) {
					Student student = (Student) students[y][x];
					Entry<Integer, CalcVector> historyState = student.nearestHistoryState(time);
					if (historyState != null) {
						student.setActualState(historyState.getValue().clone());
						student.deleteHistoryStateFrom(time);
						deleteHistoryStatStateFrom(time);
					}
				}
			}
		}
		simulateUntil(actualTime, time);
	}
	
	
	/**
	 * 
	 * Calculates the statistics for the whole course and save them in an histroy state.
	 * @param time time in milliseconds
	 * @author andres
	 */
	public void calcStatistics(int time) {
		statAvgStudentState.multiply(0); // This is done easily reset the CalcVector, without instantiation of a new one
		int studentNum = 0;
		for (IPlace[] student : students) {
			for (IPlace iplace : student) {
				if (iplace instanceof Student) {
					statAvgStudentState.addCalcVector(iplace.getActualState());
					studentNum++;
				}
			}
		}
		if (studentNum != 0) {
			statAvgStudentState.divide(studentNum);
			histStatAvgStudentStates.put(time, statAvgStudentState.clone());
			synchronized (statistics) {
				statistics.clear();
				statistics.add(new Stats(-1, "Last break:", this.lecture.getTimeBlocks().getLastBreak(time / 1000 / 60)
						+ " min"));
				for (int i = 0; i < parameters.size(); i++) {
					statistics.add(new Stats(i, parameters.get(i) + ": ", ((int) statAvgStudentState.getValueAt(i)) + " %"));
				}
			}
		}
	}
	
	/**
	 * Class for saving stat values in strings.
	 * 
	 * @author andres
	 */
	public class Stats {
		private String	name;
		private String	value;
		private int		id;
		

		public Stats(int id, String name, String value) {
			this.id = id;
			this.name = name;
			this.value = value;
		}
		
		
		public String getName() {
			return name;
		}
		
		
		public String getValue() {
			return value;
		}
		
		
		public int getId() {
			return id;
		}
	}
	

	/**
	 * 
	 * 
	 * @param time time in milliseconds
	 * @author andres
	 */
	public void deleteHistoryStatStateFrom(int time) {
		LinkedList<Integer> toDelete = new LinkedList<Integer>();
		for (Entry<Integer, CalcVector> historyStatState : histStatAvgStudentStates.entrySet()) {
			if (historyStatState.getKey() > time) {
				toDelete.add(historyStatState.getKey());
			}
		}
		for (Integer state : toDelete) {
			histStatAvgStudentStates.remove(state);
		}
	}
	
	
	/**
	 * Add student row/column at given location.
	 * location may be one of: left, right, top, bottom
	 * 
	 * @param location
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void addStudents(String location) {
		int oldX = 0;
		if (students.length > 0) {
			oldX = students[0].length;
		}
		int oldY = students.length;
		int newX = oldX;
		int newY = oldY;
		int offsetX = 0;
		int offsetY = 0;
		if (location.equals("left")) {
			newX++;
			offsetX++;
		} else if (location.equals("right")) {
			newX++;
		} else if (location.equals("top")) {
			newY++;
			offsetY++;
		} else if (location.equals("bottom")) {
			newY++;
		} else {
			logger.warn("Action performed with unkown button.");
		}
		IPlace[][] newStudents = new IPlace[newY][newX];
		for (int y = 0; y < newY; y++) {
			for (int x = 0; x < newX; x++) {
				if (x < offsetX || y < offsetY || (x - offsetX) >= oldX || (y - offsetY) >= oldY) {
					Student newStud = new Student(parameters.size());
					for (int i = 0; i < parameters.size(); i++) {
						newStud.addToChangeVector(i, (float) (Math.random() * 100) - 50);
						newStud.addValueToStateVector(i, (int) (Math.random() * 100));
					}
					newStudents[y][x] = newStud;
				} else {
					newStudents[y][x] = students[y - offsetY][x - offsetX];
				}
			}
		}
		students = newStudents;
		Observers.notifyStudents();
	}
	
	
	/**
	 * Remove student row/column at given location.
	 * location may be one of: left, right, top, bottom
	 * 
	 * @param location
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void subStudents(String location) {
		int oldX = 0;
		if (students.length > 0) {
			oldX = students[0].length;
		}
		int oldY = students.length;
		int newX = oldX;
		int newY = oldY;
		int offsetX = 0;
		int offsetY = 0;
		if (location.equals("left")) {
			newX--;
			offsetX = 1;
		} else if (location.equals("right")) {
			newX--;
		} else if (location.equals("top")) {
			newY--;
			offsetY = 1;
		} else if (location.equals("bottom")) {
			newY--;
		} else {
			logger.warn("Action performed with unkown button.");
		}
		IPlace[][] newStudents = new IPlace[newY][newX];
		for (int y = 0; y < newY; y++) {
			for (int x = 0; x < newX; x++) {
				newStudents[y][x] = students[y + offsetY][x + offsetX];
			}
		}
		students = newStudents;
		Observers.notifyStudents();
	}
	
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof String) {
			return this.getName().equals((String) o);
		} else if (o instanceof Course) {
			return this.getName().equals(((Course) o).getName());
		}
		return false;
	}
	
	
	// ##################################################################################
	// ############################# getters and setters ################################
	// ##################################################################################
	
	
	public IPlace[][] getStudents() {
		return students;
	}
	
	
	public void setStudents(IPlace[][] students) {
		this.students = students;
	}
	
	
	public Influence getInfluence() {
		return influence;
	}
	
	
	public void setInfluence(Influence influence) {
		this.influence = influence;
	}
	
	
	public Lecture getLecture() {
		return lecture;
	}
	
	
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	
	
	public LinkedList<String> getProperties() {
		return parameters;
	}
	
	
	public void setProperties(LinkedList<String> properties) {
		this.parameters = properties;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String _name) {
		try {
			CourseSaver.removeFile(this, SuperFelix.coursepath);
			this.name = _name;
			CourseSaver.saveCourse(this, SuperFelix.coursepath);
		} catch (SecurityException e) {
			logger.error("Error while renaming course:", e);
			this.name = _name; // name needs to be set no matter what.
		}
	}
	
	
	public LinkedList<Stats> getStatistics() {
		return statistics;
	}
	
	
	public void setStatistics(LinkedList<Stats> statistics) {
		this.statistics = statistics;
	}
	
	
	public IPlace getSelectedStudent() {
		return selectedStudent;
	}
	
	
	public void setSelectedStudent(IPlace selectedStudent) {
		this.selectedStudent = selectedStudent;
		Observers.notifySelectedStudent();
	}
	
	
	public int getSelectedProperty() {
		return selectedProperty;
	}
	
	
	public void setSelectedProperty(int selectedProperty) {
		this.selectedProperty = selectedProperty;
		Observers.notifySelectedStudent();
	}
	
	
	public LinkedHashMap<Integer, CalcVector> getHistStatAvgStudentStates() {
		return histStatAvgStudentStates;
	}
	
	
	public CalcVector getStatAvgStudentState() {
		return statAvgStudentState;
	}
	
	
	// ##################################################################################
	// ############################# sub classes ########################################
	// ##################################################################################
	
	/**
	 * Storage class for a don input
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * 
	 */
	private class DonInput {
		// index of selected parameter
		public int		index;
		// positiv or negativ change value
		public float	value;
		
		
		/**
		 * Create a new donInput
		 * 
		 * @param index index of selected parameter
		 * @param value positiv or negativ change value
		 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
		 */
		public DonInput(int index, float value) {
			this.index = index;
			this.value = value;
		}
	}
}

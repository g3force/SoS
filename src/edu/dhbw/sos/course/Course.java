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
	private transient LinkedHashMap<String, String>			statistics;
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
	
	
	public Course(String name) {
		this.name = name;
		init();
		
		students = new IPlace[5][7];
		parameters = new LinkedList<String>();
		parameters.add("Tireness");
		parameters.add("Loudness");
		parameters.add("Attention");
		parameters.add("Quality");
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 7; x++) {
				if ((y == 1 && x == 6) || (y == 2 && x == 6) || (y == 2 && x == 5) || (y == 3 && x == 6)
						|| (y == 4 && x == 6)) {
					students[y][x] = new EmptyPlace(parameters.size());
				} else {
					Student newStud = new Student(parameters.size());
					
					for (int i = 0; i < newStud.getActualState().size(); i++) {
						newStud.addToChangeVector(i, (float) (((Math.random() * 100) - 50) * 0.1));
						newStud.addValueToStateVector(i, (int) (Math.random() * 10));
					}
					students[y][x] = newStud;
				}
			}
		}
		influence = new Influence();
		TimeBlocks tbs = new TimeBlocks();
		tbs.add(new TimeBlock(20, BlockType.theory));
		tbs.add(new TimeBlock(15, BlockType.pause));
		tbs.add(new TimeBlock(30, BlockType.exercise));
		tbs.add(new TimeBlock(30, BlockType.group));
		tbs.add(new TimeBlock(10, BlockType.pause));
		tbs.add(new TimeBlock(20, BlockType.theory));
		lecture = new Lecture(new Date(), tbs);
		
	}
	
	
	private void init() {
		statistics = new LinkedHashMap<String, String>();
		statAvgStudentState = new CalcVector(4);
		histStatAvgStudentStates = new LinkedHashMap<Integer, CalcVector>();
		
		selectedStudent = null;
		selectedProperty = 0;
		simulating = false;
		donInputQueue = new LinkedList<DonInput>();
	}
	
	
	/**
	 * Reset the non persistent data (simulation, statistics, etc.)
	 * 
	 * @author NicolaiO
	 */
	public void reset() {
		init();
		for (int y = 0; y < students.length; y++) {
			for (int x = 0; x < students[y].length; x++) {
				students[y][x].reset();
			}
		}
	}
	
	
	private Object readResolve() {
		init();
		getPlace(0, 0).getActualState().printCalcVector("COURSE INIT");
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
	
	
	public void donInputQueue(int index, float value) {
		if (simulating) {
			logger.debug("donInput queued with index " + index + " and value " + value);
			donInputQueue.add(new DonInput(index, value));
		} else {
			donInput(index, value);
		}
	}
	
	
	// TODO: dirk, does this work? no queue needed for suggestions?
	public void suggestionInput(CalcVector calcVec) {
		logger.info("Suggestion performed");
		for (int y = 0; y < students.length; y++) {
			for (int x = 0; x < students[y].length; x++) {
				students[y][x].addToStateVector(calcVec, x, y);
			}
		}
	}
	
	
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
		simulating = true;
		students[0][0].printAcutalState();
		
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
		TimeBlock actual = lecture.getTimeBlocks().getTimeBlockAtTime(currentTime / 60000);
		logger.warn("currentTime: " + currentTime + " Block: " + actual.getType().toString() + " len: " + actual.getLen());
		preChangeVector.printCalcVector("Sim: after timeblock (" + bt.toString() + ")");
		
		// timeDending ( inf(Time) * currentTime/1000 * timeInf )
		double timeInf = 0.000000000001;
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
					// output for one student (1,1) -> only for analyzing the simulation behavior
					if (y == 1 && x == 1)
						neighborInfl.printCalcVector("Sim(1,1): Neighbor");
					
					// create a new vector which contains the pre calculates vector and the neighbor vector
					CalcVector preChangeVectorSpecial = neighborInfl.addCalcVector(preChangeVector).clone()
							.addCalcVector(neighborInfl);
					// output for one student (1,1) -> only for analyzing the simulation behavior
					if (y == 1 && x == 1)
						neighborInfl.printCalcVector("Sim(1,1): preChangeVectorSpecial = Neighbor + preChangeVector");
					
					// create a new student and let him calculate a new change vector
					student.calcNextSimulationStep(preChangeVectorSpecial, influence, currentTime, x, y);
					if (y == 1 && x == 1)
						student.getActualState().printCalcVector("Sim(1,1): actualStateEnd");
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
	
	
	/**
	 * calculates the changeVector concerning the neighbors for one student
	 * @param student
	 * @param x position of the student
	 * @param y position of the student
	 * @return changeVector
	 * @author dirk
	 */
	private CalcVector getNeighborsInfluence(Student student, CalcVector[][] oldVec, int x, int y) {
		// neighbor ( inf(Neighbor) * state(studentLeft) * neighbor + inf(Neighbor) * state(studentRight) * ... )
		
		// x x x factor for each relative position to the student
		// x o x
		// x x x
		CalcVector changeVector = new CalcVector(student.getActualState().size());
		// double[][] neighborInf = { { 0.0001, 0.0001, 0.0001 }, { 0.0010, 0.00, 0.0010 }, { 0.000010, 0.000010, 0.000010
		// } };// new double[3][3];
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 3; j++) {
		// neighborInf[j][i] = 0.01;
		// }
		// }
		// if(x==0 && y==0)
		// influence.getEnvironmentVector(EInfluenceType.NEIGHBOR, neighborInf[0][0])
		// .printCalcVector("Influence neighbor: ");
		
		// System.out.println("x: "+x+" / y: "+y);
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
						
						// add small percentage of surrounding students to every student
						// problem: will increase until infinity
						// CalcVector studentsState = s.getActualState();
						// changeVector.addCalcVector(influence.getEnvironmentVector(EInfluenceType.NEIGHBOR,
						// neighborInf[j + 1][i + 1]).multiplyWithVector(studentsState));
						
					}
				}
			}
		}
		
		// Game of life
		// 1. calculate average of surrounding students
		// 2. calculate difference of students value and average
		// 3. changeVector = difference of students value and average
		for (int i = 0; i < changeVector.size(); i++) {
			float average = surronding.getValueAt(i) / neighbourAmount;
			float studentMAverage = student.getActualState().getValueAt(i) - average;
			if (studentMAverage < 0)
				studentMAverage *= -1;
			float reducer = (100 - studentMAverage) / 100;
			if (x == 1 && y == 1)
				logger.debug("Sim(1,1): average: " + average + " / actualState: " + student.getActualState().getValueAt(i)
						+ " / reducer: " + reducer + " / value: " + (average - student.getActualState().getValueAt(i))
						* reducer * 0.1f);
			changeVector.setValueAt(i, (average - student.getActualState().getValueAt(i)) * reducer * 0.001f);
			// changeVector.multiplyWithVector(influence.getEnvironmentVector(EInfluenceType.NEIGHBOR,0.01));
		}
		if (x == 1 && y == 1)
			changeVector.printCalcVector("Sim(1,1): Change vector (neighbors): ");
		
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
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 7; b++) {
				if (students[a][b] == null) {
					isnull++;
					System.out.println(a + " / " + b);
				} else if (students[a][b] instanceof EmptyPlace)
					empty++;
				else if (students[a][b] instanceof Student)
					studentsA++;
			}
		}
		System.out.println("null: " + isnull);
		System.out.println("empty: " + empty);
		System.out.println("studentsA: " + studentsA);
	}
	
	
	/**
	 * 
	 * TODO andres, add comment!
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
		Observers.notifyStudents();
		Observers.notifySimUntil(false);
		Observers.notifyStatistics();
		logger.warn("historyStates: " + students[0][0].getHistoryStates().size());
	}
	
	
	/**
	 * 
	 * 
	 * @param actualTime current time (milliseconds)
	 * @param time new time (milliseconds)
	 * @author ???
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
		statAvgStudentState.multiply(0);
		int studentNum = 0;
		// for (IPlace[] studentRow : students) {
		// for (IPlace student : studentRow) {
		// if (student instanceof Student) {
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
			// TODO andres save history statistics.
			synchronized (statistics) {
				statistics.clear();
				statistics.put("Last break:", "unkown"); // TODO andres calculate last break
				for (int i = 0; i < parameters.size(); i++) {
					statistics.put(parameters.get(i) + ": ", ((int) statAvgStudentState.getValueAt(i)) + "");
				}
			}
			histStatAvgStudentStates.put(time, statAvgStudentState.clone());
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
	 * @author NicolaiO
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
	 * @author NicolaiO
	 */
	public void subStudents(String location) {
		int oldX = 0;
		if (students.length > 0) {
			oldX = students[0].length;
		}
		int oldY = students.length;
		int newX = oldX;
		int newY = oldY;
		if (location.equals("left")) {
			newX--;
		} else if (location.equals("right")) {
			newX--;
		} else if (location.equals("top")) {
			newY--;
		} else if (location.equals("bottom")) {
			newY--;
		} else {
			logger.warn("Action performed with unkown button.");
		}
		IPlace[][] newStudents = new IPlace[newY][newX];
		for (int y = 0; y < newY; y++) {
			for (int x = 0; x < newX; x++) {
				newStudents[y][x] = students[y][x];
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
	
	
	public LinkedHashMap<String, String> getStatistics() {
		return statistics;
	}
	
	
	public void setStatistics(LinkedHashMap<String, String> statistics) {
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
	 * @author NicolaiO
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
		 * @author NicolaiO
		 */
		public DonInput(int index, float value) {
			this.index = index;
			this.value = value;
		}
	}
}

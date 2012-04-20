/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.sos.course;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.influence.EInfluenceType;
import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;

/**
 * This class is a data holder for the data belonging to the course
 * 
 * @author DirkK
 */
public class Course {
	private IPlace[][] students;
	private Influence influence;
	private Lecture lecture;
	private HashMap<Integer, IPlace[][]> historyDonInput;
	private LinkedList<String> properties;
	private String name;
	private static final Logger	logger	= Logger.getLogger(Course.class);
	
	public Course() {
		students = new IPlace[5][7];
		properties = new LinkedList<String>();
		properties.add("Tireness");
		properties.add("Loudness");
		properties.add("Attention");
		properties.add("Quality");
		for (int y = 0; y < 5;y++) {
			for (int x = 0; x < 7;x++) {
				if(y==3) {
					students[y][x] = new EmptyPlace(properties.size());
				} else {
					Student newStud = new Student(properties);
					
					for(int i=0; i<4; i++) {
						newStud.addValueToChangeVector(i, (int)(Math.random()*100));
						newStud.addValueToStateVector(i, (int)(Math.random()*100));
					}
//					((Student)students[y][x]).
					students[y][x] = newStud;
				}
			}
		}
		
		influence = new Influence();
		historyDonInput = new HashMap<Integer, IPlace[][]>();
		lecture = new Lecture(new Date());
		lecture.getTimeBlocks().addTimeBlock(new TimeBlock(10,BlockType.theory));
		lecture.getTimeBlocks().addTimeBlock(new TimeBlock(20,BlockType.pause));
		lecture.getTimeBlocks().addTimeBlock(new TimeBlock(30,BlockType.exercise));
		lecture.getTimeBlocks().addTimeBlock(new TimeBlock(10,BlockType.pause));
		lecture.getTimeBlocks().addTimeBlock(new TimeBlock(30,BlockType.group));
	}
	
	
	
	/**
	 * adds a new state to the history states
	 * @param time
	 * @param currentState
	 * @author dirk
	 */
	public void addHistoryDonInput(int time, IPlace[][] currentState) {
		historyDonInput.put(time, currentState);
	}
	
	/**
	 * takes a start and end time
	 * if there was an interaction from the don in this time period the latest interaction will be returned
	 * otherwise null will be returned 
	 * @param start time in milliseconds
	 * @param end time in milliseconds
	 * @return 
	 * @author dirk
	 */
	public Entry<Integer, IPlace[][]> historyDonInputInInterval(int start, int end) {
		Entry<Integer, IPlace[][]> latest = null;
		for(Entry<Integer, IPlace[][]> historyState : historyDonInput.entrySet())
		{
			if(historyState.getKey()>start && historyState.getKey()<end) {
				if(latest==null || latest.getKey()<historyState.getKey())
					latest = historyState;
			}
		}
		return latest;
	}
	
	public void simulationStep(int currentTime, int speed) {
		
			students[0][0].printAcutalState();
			// check if there was an interaction from the don
			Entry<Integer, IPlace[][]> donInteraction = historyDonInputInInterval(currentTime - speed, currentTime);
			if (donInteraction != null) {
				students = donInteraction.getValue();
			}
			//the new array for the calculated students
			IPlace[][] newState = new IPlace[students.length][students[0].length];
			for (int y = 0; y < 5;y++) {
				for (int x = 0; x < 7;x++) {
					newState[y][x] = new EmptyPlace(properties.size());
				}
			}
			
			//student independent calculations
			CalcVector preChangeVector = new CalcVector(properties);
			preChangeVector.printCalcVector("Init");
			// - - - breakReaction -> inf(Break) * breakInf
			double breakInf = 0.01;
			if(lecture.getTimeBlocks().getTimeBlockAtTime(currentTime/60000).getType() == BlockType.pause) {
				logger.info("Influenced by break");
				preChangeVector.addCalcVector(influence.getEnvironmentVector(EInfluenceType.BREAK_REACTION, breakInf));
			}
			preChangeVector.printCalcVector("after break");
			
			// - - - timeDending -> inf(Time) * currentTime/1000 * timeInf
			double timeInf = 0.001;
			double timeTimeInf = timeInf * currentTime / 1000;
			preChangeVector.addCalcVector(influence.getEnvironmentVector(EInfluenceType.TIME_DEPENDING, timeTimeInf));
			preChangeVector.printCalcVector("after time depending");
			
			// iterate over all students
			for (int y = 0; y < students.length; y++) {
				for (int x = 0; x < students[y].length; x++) {
					if(students[y][x] instanceof Student) {
						//influence of the surrounding students
						CalcVector neighborInfl = getNeighborsInfluence((Student)students[y][x], x, y);
						if(y==0 && x==0)
							neighborInfl.printCalcVector("Neighbor");
						CalcVector preChangeVectorSpecial = neighborInfl.addCalcVector(preChangeVector);
						if(y==0 && x==0)
							neighborInfl.printCalcVector("preChangeVectorSpecial = Neighbor + preChangeVector");
						
						//create a new student and let him calculate a new change vector
						newState[y][x] = ((Student)students[y][x]).clone();
						((Student)newState[y][x]).calcNextSimulationStep(preChangeVectorSpecial, influence, x,y);
						if(y==0 && x==0)
							((Student)newState[y][x]).printAcutalState();
						((Student)students[y][x]).saveHistoryStates(currentTime);					}
				}
			}
			//give the reference from newState to real students array
			students = newState;
			
	}
	
	/**
	 * calculates a changeVector for one students
	 * @param student
	 * @param x position of the student
	 * @param y 
	 * @return
	 * @author dirk
	 */
	private CalcVector getNeighborsInfluence(Student student, int x, int y) {
	// - - - neighbor -> inf(Neighbor) * state(studentLeft) * neighbor + inf(Neighbor) * state(studentRight) * neighbor
		
			CalcVector changeVector = new CalcVector(student.getActualState().size());
			double[][] neighborInf = new double[3][3];
			for(int i = 0; i<3; i++) {
				for(int j = 0; j<3; j++) {
					neighborInf[j][i] = 0.01;
				}
			}
			
			influence.getEnvironmentVector(EInfluenceType.NEIGHBOR, neighborInf[0][0]).printCalcVector("Influence neighbor: ");
			
			//System.out.println("x: "+x+" / y: "+y);
			for(int i = -1; i<=1; i++) {
				for(int j = -1; j<=1; j++) {
					if(i!=0 || j!=0) {
						int newx = x+i;
						int newy = y+j;
						if(newx < students[0].length && newx >= 0 && newy < students.length && newy >= 0) {
							//System.out.println("newx: "+newx+" / newy: "+newy);
							IPlace s = students[newy][newx];
							CalcVector studentsState = s.getActualState();
							changeVector.addCalcVector(influence.getEnvironmentVector(EInfluenceType.NEIGHBOR, neighborInf[j+1][i+1])
									.multiplyWithVector(studentsState));
							
						}
					}
				}
			}
			return changeVector;
	}
	
	public void printFieldInformation() {
		int isnull = 0;
		int studentsA= 0;
		int empty= 0;
		for (int a = 0; a < 5;a++) {
			for (int b = 0; b < 7;b++) {
				if(students[a][b]==null) {
					isnull++;
					System.out.println(a+" / "+b);
				} else if (students[a][b] instanceof EmptyPlace)
					empty++;
				else if (students[a][b] instanceof Student)
					studentsA++;
			}
		}
		System.out.println("null: "+isnull);
		System.out.println("empty: "+empty);
		System.out.println("studentsA: "+studentsA);
		
	}
	
	// --- GETTERS and SETTERS ---
	
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
		return properties;
	}

	public void setProperties(LinkedList<String> properties) {
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}

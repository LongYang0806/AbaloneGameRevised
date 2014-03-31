package org.abalone.client;

import static org.abalone.client.AbaloneConstants.B;
import static org.abalone.client.AbaloneConstants.E;
import static org.abalone.client.AbaloneConstants.I;
import static org.abalone.client.AbaloneConstants.S;
import static org.abalone.client.AbaloneConstants.W;
import static org.abalone.client.AbaloneConstants.BTurn;
import static org.abalone.client.AbaloneConstants.WTurn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class AbaloneStateTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		
		List<ArrayList<Integer>> jumps = Lists.<ArrayList<Integer>>newArrayList(
				Lists.newArrayList(7, 7, 6, 8, 1), 
				Lists.newArrayList(8, 6, 7, 7, 1));
		AbaloneState startState = new AbaloneState(WTurn, Lists.newArrayList("0", "1"), 
				AbaloneConstants.initialBoard, Lists.<ArrayList<Integer>>newArrayList(), null);
		
		List<ArrayList<String>> targetBoard =
	  		Lists.<ArrayList<String>>newArrayList(
	  				Lists.newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I),
	  				Lists.newArrayList(I, I, I, S, S, W, I, W, I, E, I, B, I, B, S, S, I, I, I),
	  				Lists.newArrayList(I, I, S, S, W, I, W, I, W, I, B, I, B, I, B, S, S, I, I),
	  				Lists.newArrayList(I, S, S, E, I, W, I, W, I, E, I, B, I, B, I, E, S, S, I),
	  				Lists.newArrayList(S, S, E, I, E, I, E, I, E, I, E, I, E, I, E, I, E, S, S),
	  				Lists.newArrayList(S, E, I, E, I, E, I, E, I, E, I, E, I, E, I, E, I, E, S),
	  				Lists.newArrayList(S, S, E, I, E, I, E, I, B, I, E, I, E, I, E, I, E, S, S),
	  				Lists.newArrayList(I, S, S, E, I, B, I, B, I, E, I, W, I, W, I, E, S, S, I),
	  				Lists.newArrayList(I, I, S, S, B, I, E, I, B, I, W, I, W, I, W, S, S, I, I),
	  				Lists.newArrayList(I, I, I, S, S, B, I, B, I, E, I, W, I, W, S, S, I, I, I),
	  				Lists.newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I)
	  		);
		
		AbaloneState endState = startState.applyJumpOnBoard(jumps);
		
		assertEquals(endState.getBoard(), targetBoard);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIsGameEnd() {
		List<ArrayList<String>> startBoard = 
	  		Lists.<ArrayList<String>>newArrayList(
	  				Lists.<String>newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I),
	  				Lists.<String>newArrayList(I, I, I, S, S, E, I, B, I, W, I, B, I, E, S, S, I, I, I),
	  				Lists.<String>newArrayList(I, I, S, S, E, I, W, I, W, I, W, I, W, I, E, S, S, I, I),
	  				Lists.<String>newArrayList(I, S, S, W, I, W, I, W, I, B, I, B, I, B, I, E, S, S, I),
	  				Lists.<String>newArrayList(S, S, E, I, E, I, E, I, B, I, B, I, B, I, W, I, W, S, S),
	  				Lists.<String>newArrayList(S, E, I, E, I, E, I, E, I, E, I, E, I, B, I, E, I, E, S),
	  				Lists.<String>newArrayList(S, S, E, I, E, I, W, I, E, I, E, I, B, I, E, I, E, S, S),
	  				Lists.<String>newArrayList(I, S, S, E, I, B, I, E, I, E, I, E, I, E, I, E, S, S, I),
	  				Lists.<String>newArrayList(I, I, S, S, E, I, E, I, W, I, W, I, B, I, B, S, S, I, I),
	  				Lists.<String>newArrayList(I, I, I, S, S, B, I, W, I, E, I, E, I, E, S, S, I, I, I),
	  				Lists.<String>newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I)
	  		);
		
		List<ArrayList<String>> targetBoard = 
				Lists.<ArrayList<String>>newArrayList(
	  				Lists.<String>newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I),
	  				Lists.<String>newArrayList(I, I, I, S, S, E, I, B, I, W, I, B, I, E, S, S, I, I, I),
	  				Lists.<String>newArrayList(I, I, S, S, E, I, W, I, W, I, W, I, W, I, E, S, S, I, I),
	  				Lists.<String>newArrayList(I, S, S, W, I, W, I, W, I, B, I, B, I, B, I, E, S, S, I),
	  				Lists.<String>newArrayList(S, S, E, I, E, I, E, I, E, I, B, I, B, I, B, I, W, W, S),
	  				Lists.<String>newArrayList(S, E, I, E, I, E, I, E, I, E, I, E, I, B, I, E, I, E, S),
	  				Lists.<String>newArrayList(S, S, E, I, E, I, W, I, E, I, E, I, B, I, E, I, E, S, S),
	  				Lists.<String>newArrayList(I, S, S, E, I, B, I, E, I, E, I, E, I, E, I, E, S, S, I),
	  				Lists.<String>newArrayList(I, I, S, S, E, I, E, I, W, I, W, I, B, I, B, S, S, I, I),
	  				Lists.<String>newArrayList(I, I, I, S, S, B, I, W, I, E, I, E, I, E, S, S, I, I, I),
	  				Lists.<String>newArrayList(I, I, I, I, S, I, S, I, S, I, S, I, S, I, S, I, I, I, I)
	  		);
		
		List<ArrayList<Integer>> jumps = 
				Lists.<ArrayList<Integer>>newArrayList(
						Lists.<Integer>newArrayList(4, 16, 4, 17, 0), 
						Lists.<Integer>newArrayList(4, 14, 4, 16, 0),
						Lists.<Integer>newArrayList(4, 12, 4, 14, 1),
						Lists.<Integer>newArrayList(4, 10, 4, 12, 1),
						Lists.<Integer>newArrayList(4, 8, 4, 10, 1)
				);
		
		AbaloneState startState = new AbaloneState(BTurn, Lists.newArrayList("0", "1"), 
				startBoard, Lists.<ArrayList<Integer>>newArrayList(), null);
		
		AbaloneState afterState = startState.applyJumpOnBoard(jumps);
		
		assertEquals(afterState.getBoard(), targetBoard);
		assertTrue(afterState.getIsGameEnd().get());
	}

}

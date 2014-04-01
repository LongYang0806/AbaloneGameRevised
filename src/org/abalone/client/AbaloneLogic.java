package org.abalone.client;

import static org.abalone.client.AbaloneConstants.BTurn;
import static org.abalone.client.AbaloneConstants.WTurn;
import static org.abalone.client.AbaloneConstants.illegalSquares;
import static org.abalone.client.AbaloneConstants.scoreSquares;
import static org.abalone.client.AbaloneConstants.initialBoard;
import static org.abalone.client.AbaloneConstants.BOARD;
import static org.abalone.client.AbaloneConstants.JUMP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.game_api.GameApi.EndGame;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi.VerifyMoveDone;

import com.google.common.collect.Lists;

public class AbaloneLogic {

	public VerifyMoveDone verify(VerifyMove verifyMove) {
    try {
      checkMoveIsLegal(verifyMove);
      return new VerifyMoveDone();
    } catch (Exception e) {
    	e.printStackTrace(System.out);
      return new VerifyMoveDone(verifyMove.getLastMovePlayerId(), e.getMessage());
    }
  }
	
	void checkMoveIsLegal(VerifyMove verifyMove) {
		// First, get all the necessary information:
		Map<String, Object> state = verifyMove.getState();
		Map<String, Object> lastState = verifyMove.getLastState();
		List<Operation> lastMove = verifyMove.getLastMove();
		List<String> playerIds = verifyMove.getPlayerIds();
		String lastMovePlayerId = verifyMove.getLastMovePlayerId();
		String turn = getTurn(playerIds, lastMovePlayerId);
		
		if(lastState.isEmpty()){
			// If no last State, this means that we just started the game.
			check(lastMovePlayerId.equals(playerIds.get(0)), 
					"The player of the initial operations must be the first player!");
			check(lastMove.size() == 3 && 
						lastMove.get(0) instanceof SetTurn && 
						lastMove.get(1) instanceof Set &&
						lastMove.get(2) instanceof Set, 
					"The initial operations should contains three operations: SetTurn, Set, Set");
			System.out.println("Initialization");
			return;
		}
		
		/*
  	 * Structure of lastMove:
  	 * 	SetTurn
  	 * 	Board
  	 * 	Jump
  	 * 	(EndGame)
  	 */
		// 1. The transformation should be correct.
		@SuppressWarnings("unchecked")
		List<ArrayList<Integer>> jumps = (List<ArrayList<Integer>>) ((Set)lastMove.get(2)).getValue();
		AbaloneState abaloneStateLast = 
				AbaloneState.gameApiState2AbaloneState(lastState, turn, playerIds);
		System.out.println("******" + jumps);
		AbaloneState abaloneStateNow = 
				AbaloneState.gameApiState2AbaloneState(state, turn, playerIds);
		AbaloneState abaloneStateTransformed = abaloneStateLast.applyJumpOnBoard(jumps);
		
		System.out.println("Logic Jump");
		for(int i = 0; i < jumps.size(); i++){
			for(int j = 0; j < jumps.get(i).size(); j++) {
				System.out.print(jumps.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println("Logic Board Last");
		for(int i = 0; i < abaloneStateLast.getBoard().size(); i++){
			for(int j = 0; j < abaloneStateLast.getBoard().get(i).size(); j++) {
				System.out.print(abaloneStateLast.getBoard().get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println("Logic Board Now");
		for(int i = 0; i < abaloneStateNow.getBoard().size(); i++){
			for(int j = 0; j < abaloneStateNow.getBoard().get(i).size(); j++) {
				System.out.print(abaloneStateNow.getBoard().get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println("Logic Board Transformed");
		for(int i = 0; i < abaloneStateTransformed.getBoard().size(); i++){
			for(int j = 0; j < abaloneStateTransformed.getBoard().get(i).size(); j++) {
				System.out.print(abaloneStateTransformed.getBoard().get(i).get(j) + " ");
			}
			System.out.println();
		}
		
		check(abaloneStateNow.equals(abaloneStateTransformed), 
				"LastState applied lastMove should get current State");		
		
		
		// 2. Focus on the {@code lastMove}
		check( lastMove.get(0) instanceof SetTurn && 
					 lastMove.get(1) instanceof Set && 
					 lastMove.get(2) instanceof Set, 
					 "The structure of the lastMove should be 'SetTurn, Board, Jump, (EndGame)'");
		check( (lastMove.size() == 3) || 
					 (lastMove.size() == 4 && lastMove.get(3) instanceof EndGame), 
				"The structure of the lastMove should be 'SetTurn, Board, Jump, (EndGame)'");
		
		// 3, Focus on the {@code jumps}
		checkJump(jumps);
	}
	
	public void checkJump(List<ArrayList<Integer>> jumps) {
		if(jumps != null && !jumps.isEmpty()) {
		
			// 1. check for format, illegal square and score square
			for(List<Integer> jump : jumps) {
				check(jump.size() == 5, 
						"Each jump item should be the format '{startX, startY, endX, endY, 0/1}'");
				check(jump.get(4) == 0 || jump.get(4) == 1, 
						"Each jump item's last digit should be 0/1 for piece color");
				check(!illegalSquares.contains(Lists.<Integer>newArrayList(jump.get(0), jump.get(1))), 
						"start coordinates should not locate inside illegal squares");
				check(!illegalSquares.contains(Lists.<Integer>newArrayList(jump.get(2), jump.get(3))), 
						"end coordinates should not locate inside illegal squares");
				check(!scoreSquares.contains(Lists.<Integer>newArrayList(jump.get(0), jump.get(1))), 
						"start coordinates should not locate inside score squares");
			}
			
			// 2. all the jumps should locate in the one direction.
			if(jumps.size() > 1) {
				if(jumps.get(0).get(0) == jumps.get(1).get(0)) {
					// horizontal
					for(int i = 1; i < jumps.size(); i++) {
						check(jumps.get(i).get(0) == jumps.get(i - 1).get(0) && 
									jumps.get(i).get(2) == jumps.get(i - 1).get(2),
								"Horizontal: X coordinate should be the same!");
					}
				} else {
					// diagonal
					for(int i = 1; i < jumps.size(); i++) {
						check(Math.abs(jumps.get(i).get(0) - jumps.get(i - 1).get(0)) == 1 && 
									Math.abs(jumps.get(i).get(1) - jumps.get(i - 1).get(1)) == 1 &&
									Math.abs(jumps.get(i).get(2) - jumps.get(i - 1).get(2)) == 1 &&
									Math.abs(jumps.get(i).get(3) - jumps.get(i - 1).get(3)) == 1, 
								"Diagonal: coordiante difference should always be one");
					}
				}
			}
		}
	}
	
	/**
	 * Method used to get turn based on {@code playerIds} and {@code lastMovePlayerId}
	 * 
	 * @param playerIds
	 * @param lastMovePlayerId
	 * @return
	 */
	public String getTurn(List<String> playerIds, String lastMovePlayerId) {
		return playerIds.indexOf(lastMovePlayerId) == 0 ? WTurn : BTurn;
	}
	/**
	 * Method used to check whether the given condition is true.
	 * @param condition to be tested condition
	 * @param message optional debug messages.
	 * @throws RuntimeException if the give {@code condition} is false.
	 */
	private void check(boolean condition, String... message){
		if(!condition){
			throw new RuntimeException("Hacker found: " + message);
		}
	}
	
	public List<Operation> getInitialMove(List<String> playerIds) {
		String wpId = playerIds.get(0);
		return Lists.<Operation>newArrayList(
				new SetTurn(wpId),
				new Set(BOARD, initialBoard),
				new Set(JUMP, Lists.<ArrayList<Integer>>newArrayList())
		);
	}
}

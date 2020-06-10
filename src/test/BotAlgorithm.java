package test;

import java.math.*;

public class BotAlgorithm {
	
	private int board[][];
	private int weight[][];
	private int botPlayer;
	private int boardSize;
	private int isFirstStep;
	
	public BotAlgorithm() { this(new OmokState(19)); }
	public BotAlgorithm( OmokState input)
	{
		board = input.board;
		boardSize = input.size;
		weight = new int [boardSize][boardSize];
		botPlayer = input.botChoose;
		
		weight[boardSize/2-1][boardSize/2-1] = 1;
		weight[boardSize/2-1][boardSize/2] = 1;
		isFirstStep = 0;
	}
	
	/*private void default_weight()
	{
		for(int i = 0; i< boardSize; i++)
			for(int j = 0; j< boardSize; j++)
			{
				if(board[i][j] != 0)
					weight[i][j] = -1000; // worst weight = already exist
				else { //Default weight: near by bot's stone
					if(!OutOfRange(i+1)) {
						if( !OutOfRange(j+1) && board[i][j] == board[i+1][j+1] )
							weight[i][j] = 1;
						if( board[i][j] == board[i+1][j] )
							weight[i][j] = 1;
						if( !OutOfRange(j-1) && board[i][j] == board[i+1][j-1] )
							weight[i][j] = 1;
					}
					
					if( !OutOfRange(j-1) && board[i][j] == board[i][j-1])
						weight[i][j] = 1;
					if( !OutOfRange(j+1) &&  board[i][j] == board[i][j+1])
						weight[i][j] = 1; 
					
					if(!OutOfRange(i-1)) {
						if( !OutOfRange(j-1) && board[i][j] == board[i-1][j-1])
							weight[i][j] = 1;
						if( board[i][j] == board[i-1][j])
							weight[i][j] = 1;
						if( !OutOfRange(j+1) &&  board[i][j] == board[i-1][j+1])
							weight[i][j] = 1;
					}
				}
			}
	}*/
	
	protected int[] calcul_weight()
	{
		int step = 0;
		int[] stepCount = new int[8];
		int[] isBlock = new int[8];
		boolean doneCheck = false;
		int r, c;
		int sumOfWeight = 0;
		int possibility[] = { 0, 0, 0 };
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				//stepCount reset 
				for(int i = 0; i<8; i++) {
					stepCount[i] = 0;
					isBlock[i] = 0;
				}
				
				r = row;
				c = col;
				
				if(board[row][col] != 0) {
					weight[row][col] = -100;
					continue;
				}
						
				while (!doneCheck) {
					switch (step) {
					// if문에서는 step이 살펴볼 방향을 지정하며, r과 c를 수정하면서 순차적으로 살펴보며 놓여진 돌의 갯수를 stepCount의 결과를 낸다.
					// else문에서는 step을 다음 단계로 지정하며, r와 c를 초기 row값으로 되돌려 놓는다.
					// 예시. 흑돌이 놓은 자리 위에 흑돌이 셋, 아래에 흑돌이 하나 있으면 case0는 탐색 세번 = 초기화 한번, case1은 탐색 1번 초기화 
					//													되어야 하는데.... if문 아래로 전혀 들어가지 않는다.
					case 0:
						if (OutOfRange(r-1) && sameColor(--r, c))	//여긴 왜 r--? 북쪽을 쭉 갈거기 때문에 --- case 0은 여러번 호출된다.
							stepCount[step]+=10;						// if문 안으로 가질 않기 때문에 stepCount가 증가 되지 않는다 - 조건문에 문제 확인.
						else if (OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }			// 문제 해결: 두 조건문 함수에는 이상X. 인수를 살펴봄 --- 이상 발견
						break;
					case 1:
						if (OutOfRange(r+1) && sameColor(++r, c))
							stepCount[step]+=10;
						else if (OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 2:
						if (OutOfRange(c+1) && sameColor(r, ++c))
							stepCount[step]+=10;
						else if (OutOfRange(c) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 3:
						if (OutOfRange(c-1) && sameColor(r, --c))
							stepCount[step]+=10;
						else if (OutOfRange(c) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 4:
						if (OutOfRange(r-1) && OutOfRange(c+1) && sameColor(--r, ++c))
							stepCount[step]+=10;
						else if ( OutOfRange(c) && OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 5:
						if (OutOfRange(r+1) && OutOfRange(c-1) && sameColor(++r, --c))
							stepCount[step]+=10;
						else if ( OutOfRange(c) && OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 6:
						if (OutOfRange(r-1) && OutOfRange(c-1) && sameColor(--r, --c))
							stepCount[step]+=10;
						else if ( OutOfRange(c) && OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					case 7:
						if (OutOfRange(r+1) && OutOfRange(c+1) && sameColor(++r, ++c))
							stepCount[step]+=10;
						else if ( OutOfRange(c) && OutOfRange(r) && diffColor(r, c) == 1)
						{ isBlock[step] = 1; step++; r = row; c = col;}
						else { step++; r = row; c = col; }
						break;
					default:
						doneCheck = true;
						break;
					}
				}
				
				doneCheck = false;
				step = 0;
						
				for(int i = 0; i< 8; i++) {
					if(isBlock[i] == 1)
						stepCount[i] /= 2;
					sumOfWeight += stepCount[i];
				}
				if(weight[row][col] != 1)
					weight[row][col] = sumOfWeight;
				else
				{
					possibility[2] = 1;
					possibility[1] = col;
					possibility[0] = row;
				}
				
				if(possibility[2] < sumOfWeight) {
					possibility[2] = sumOfWeight;
					possibility[1] = col;
					possibility[0] = row;
				}
						
				sumOfWeight = 0;
			}
			System.out.println("line check end: "+row);
		}
		System.out.println("process end");
		return possibility;
	}
	
	private boolean sameColor(int a, int b)
	{
		if(board[a][b] == botPlayer)
			return true;
		else 
			return false;
	}
	
	private int diffColor(int a, int b)
	{
		if(board[a][b] != 0) {
		if(board[a][b] != botPlayer)
			return 1;
		else 
			return 2;
		}
		else return 3;
	}
	
	private boolean OutOfRange(int i) // check array's range  
	{
		if(i<0 || i>=boardSize) return false;
		else return true;
	}
}

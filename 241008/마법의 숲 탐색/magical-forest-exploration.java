import java.io.*;
import java.util.*;

public class Main {
	static int[][] map;
	static int[] low;
	static int R, C, K;
	
	static int[] dx = {-1, 0, 1, 0}; //북 동 남 서   
	static int[] dy = {0, 1, 0, -1};
	static int[][] exit;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int answer = 0;
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[R+3][C];
		low = new int[K];
		exit = new int[K][2];
		for(int i=1; i<=K; i++) {
			st = new StringTokenizer(br.readLine());
			int c = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken());
			
			int[] xyd = deep(i, 0, c, d);
			if(xyd[0]<=3) {
				map = new int[R+3][C];
				continue;
			}
			else {
				map[xyd[0]][xyd[1]] = i;
				for(int nd = 0; nd<4; nd++) {
					map[xyd[0]+dx[nd]][xyd[1]+dy[nd]] = i;
				}
			}
			low[i-1] = xyd[0]+1-2;
			exit[i-1] = new int[] {xyd[0]+dx[xyd[2]], xyd[1]+dy[xyd[2]]};
			findMax(i, exit[i-1][0], exit[i-1][1], new boolean[K]);
			answer+=low[findMax(i, exit[i-1][0], exit[i-1][1], new boolean[K])-1];
		}
		

		System.out.println(answer);
				
	}
	// d는 북동남서
	static int[] deep(int i, int x, int y, int d) {
		int[] xyd = {x,y,d}; 
		if(south(x,y)) { //하
			xyd = deep(i,x+1,y,d);
		}else if(west(x, y)) { //좌
			xyd = deep(i,x+1,y-1,(d+3)%4);
		}else if(east(x, y)) { //우
			xyd = deep(i,x+1,y+1,(d+1)%4);
		}
		return xyd;
	}
	static boolean south(int x, int y) {
		if(x+2<R+3 && map[x+2][y]==0 && map[x+1][y-1]==0 
				&& map[x+1][y+1]==0) return true;
		return false;
	}
	static boolean west(int x, int y) {
		if(y-2>=0 && x+2<R+3 && map[x][y-2]==0 && map[x+1][y-1]==0
				&& map[x+1][y-2]==0 && map[x+2][y-1]==0) return true;
		return false;
	}
	static boolean east(int x, int y) {
		if(y+2<C && x+2<R+3 && map[x][y+2]==0 && map[x+1][y+1]==0
				&& map[x+1][y+2]==0 && map[x+2][y+1]==0) return true;
		return false;
	}
	static int findMax(int i, int x, int y, boolean[] visit) { 
		visit[i-1] = true;
		for(int nd = 0; nd<4; nd++) {
			if(x+dx[nd]<0||x+dx[nd]>=R+3||y+dy[nd]<0||y+dy[nd]>=C) continue;
			int n = map[x+dx[nd]][y+dy[nd]];
			if(n!=0 && !visit[n-1]) {
				low[i-1] = Integer.max(low[i-1],low[findMax(n, exit[n-1][0], exit[n-1][1], visit)-1]); 
			}
			
		}
		return i;
		
	}
}
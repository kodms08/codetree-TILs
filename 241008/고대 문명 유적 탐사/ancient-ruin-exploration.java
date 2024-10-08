import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int[][] map = new int[5][5];
	static boolean[][] visit;
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	static int idx = 0;
	static int[] wall;

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();
		
		int K = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<5; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<5; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		wall = new int[M];
		st = new StringTokenizer(br.readLine());
		for(int w=0; w<M; w++) {
			wall[w] = Integer.parseInt(st.nextToken());
		}
		
		for(int k=0; k<K; k++) {
			int answer = 0; 
			int[] maxSet = new int[4]; // 값, 행, 열, 각도
			for(int x=1; x<4; x++) {
				for(int y=1; y<4; y++) {
					int[][] m = copyMap();
					for(int t=0; t<3; t++) {
						m = turn(m,x,y);
						int result = find(m);
						if(result>maxSet[0]) maxSet = new int[] {result, x, y, t};
						else if(result==maxSet[0]) {
							if(t<maxSet[3]) maxSet = new int[] {result, x, y, t};
							else if(t==maxSet[3]) {
								if(y<maxSet[2]) maxSet = new int[] {result, x, y, t};
								else if(y==maxSet[2]) {
									if(x<maxSet[1]) maxSet = new int[] {result, x, y, t};

								}
							}
						}
						
					}

				}
			}
			
			if(maxSet[0]==0) continue;
			for(int i=0; i<maxSet[3]+1; i++) {
				map = turn(map, maxSet[1], maxSet[2]);
			}
			
			remove();

			while(findEmpty()>0) {
				int e = findEmpty();
				answer+=e;
				fill(e);
				remove();
			}
			sb.append(answer).append(" ");
		
		}
		System.out.println(sb);
		

	}

	static int findEmpty() {
		int empty = 0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(map[i][j]==0) empty++;
			}
		}
		return empty;
	}
	
	static void remove() {
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				int v = map[i][j];
				visit = new boolean[5][5];
				int n = removeDFS(i,j,1);
				if(n<3) {
					backDFS(i,j,v);
				}
				
			}
		}
	}
		
	
	static void backDFS(int x, int y, int v) {
		map[x][y] = v;
		for(int d = 0; d<4; d++) {
			if(x+dx[d]<0||x+dx[d]>=5||y+dy[d]<0|| y+dy[d]>=5||
					!visit[x+dx[d]][y+dy[d]]) continue;
			
			if(map[x+dx[d]][y+dy[d]]==0) {
				backDFS(x+dx[d], y+dy[d], v);
			}
		}
	}
	static int removeDFS(int x, int y, int c) {
		visit[x][y] = true;
		int n = map[x][y];
		map[x][y] = 0;
		for(int d = 0; d<4; d++) {
			if(x+dx[d]<0||x+dx[d]>=5||y+dy[d]<0|| y+dy[d]>=5||
					visit[x+dx[d]][y+dy[d]]) continue;
			
			if(n==map[x+dx[d]][y+dy[d]]) {
				c+=removeDFS(x+dx[d], y+dy[d], 1);
			}
		}
		
		return c;
		
	}
	
	static void fill(int empty) {
		while(empty>0) {
			for(int j=0; j<5; j++) {
				for(int i=4; i>=0; i--) {
					if(map[i][j]==0) {
						map[i][j] = wall[idx];
						idx++;
						empty--;
					}
				}
			}
		}
	}
	
	static int[][] turn(int[][] m, int x, int y) {
		int[] cx = {-1, -1, -1, 0, 1, 1, 1, 0};
		int[] cy = {-1, 0, 1, 1, 1, 0, -1, -1};
		Queue<Integer> queue = new LinkedList<Integer>();
		for(int i=0; i<8; i++) {
			queue.add(m[x+cx[i]][y+cy[i]]);
		}
		
		for(int i=2; i<10; i++) {
			m[x+cx[i%8]][y+cy[i%8]] = queue.poll();
		}
		return m;
	}
	
	static int find(int[][] m) {
		int result = 0;
		visit = new boolean[5][5];
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(visit[i][j]) continue;
				int n = dfs(m, i, j, 1);
				if(n>=3) result+=n;
			}
		}
		
		return result;
		
	}
	
	static int dfs(int[][] m, int x, int y, int c) {
		visit[x][y] = true;
		for(int d=0; d<4; d++) {
			if(x+dx[d]<0||x+dx[d]>=5||y+dy[d]<0|| y+dy[d]>=5||
					visit[x+dx[d]][y+dy[d]]) continue;
			if(m[x][y]==m[x+dx[d]][y+dy[d]]) {
				c+= dfs(m, x+dx[d], y+dy[d], 1);
			}
		}
		return c ;
		
	}
	
	
	static int[][] copyMap() {
		int[][] m = new int[5][5];
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				m[i][j] = map[i][j];
			}
		}
		return m;
	}

}
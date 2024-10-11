import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static class Top{
		int r;
		int c;
		int p;
		int last;
		public Top(int r, int c, int p, int last) {
			super();
			this.r = r;
			this.c = c;
			this.p = p;
			this.last = last;
		}
		@Override
		public String toString() {
			return "Top [r=" + r + ", c=" + c + ", p=" + p + ", last=" + last + "]";
		}
	}
	
	static int[][] map;
	static boolean[][] visit;
	static HashMap<Integer, Top> tops = new HashMap<>();
	static int N, M;
	
	static int dx[] = {0, 1, 0, -1}; //우하좌상
	static int dy[] = {1, 0, -1, 0};

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		map = new int[N][M]; //포탑 유무
		int n=1;
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<M; j++) {
				int v = Integer.parseInt(st.nextToken());
				if(v>0) {
					map[i][j]=n;
					tops.put(n++, new Top(i,j,v,0));
				}
			}
		}

		for(int k=0; k<K; k++) {
			
			//포탑 선정
			Top[] lowHigh = findTwoTop();
			Top low = lowHigh[0];
			low.last=k+1;
			low.p+=N+M;
			Top high = lowHigh[1];
			
			//포탑 공격
			visit = new boolean[N][M]; //공격 유무
			int distance = finMinDistance(low, high); //최단거리 찾기
			visit = new boolean[N][M]; //공격 유무
			if(distance>0) { //레이저 공격
				laser(0, distance, low.r, low.c, 0, low, high);
			}else { ///포탄 공격
				bomb(low, high);
			}
			
			if(tops.size()==1) break;
			
			//포탑정비
			refactory();

		}
			
		int answer = 0;
		for(Top t: tops.values()) {
			if(t.p>answer) answer=t.p;
		}
		System.out.println(answer);
	}
	
	static void refactory() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(map[i][j]>0&&!visit[i][j]) {
					tops.get(map[i][j]).p++;
				}
			}
		}
	}
	static void bomb(Top low, Top high) {
		visit[low.r][low.c] = true;
		for(int i=-1; i<2; i++) {
			for(int j=-1; j<2; j++) {
				int r=high.r+i;
				int c=high.c+j;
				if(r<0) r=N-r;
				else if(r>=N) r=r-N;
				if(c<0) c=M-c;
				else if(c>=M) c=c-M;
				
				if(map[r][c]==0) continue;
				visit[r][c]=true;
				if(r==high.r&& c==high.c) {
					high.p-=low.p;
					demageTop(high);
				}else {
					Top t = tops.get(map[r][c]);
					t.p-=low.p/2;
					demageTop(t);
				}
			}
		}
	}

	static boolean laser(int dir, int distance, int r, int c, int d, Top low, Top high) {
		if(r==high.r&&c==high.c) {
			high.p-=low.p;
			demageTop(high);
			visit[r][c]=true;
			return true;
		}
		visit[r][c] = true;
		if(d==distance) return false;
		
		for(int i=dir; i<dir+4; i++) {
			int nd=i%4;
			int nr = r+dx[nd];
			int nc = c+dy[nd];
			if(nr<0) nr+=N;
			else if(nr>=N) nr-=N;
			if(nc<0) nc+=M;
			else if(nc>=M) nc-=M;
			
			if(visit[nr][nc]||map[nr][nc]==0) continue;
			if(laser(nd, distance, nr, nc, d+1, low, high)) {
				if(nr!=high.r||nc!=high.c) {
					Top t = tops.get(map[nr][nc]);
					t.p-=(low.p/2);
					demageTop(t);
				}
				return true;
			}
			visit[nr][nc]=false;
		}
		return false;
	}
	
	static void demageTop(Top t) {
		if(t.p<=0) {
			tops.remove(map[t.r][t.c]);
			map[t.r][t.c]=0;
		}
	}
	
	static int finMinDistance(Top low, Top high) {
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] {low.r, low.c, 0});
		visit[low.r][low.c] = true;
		
		while(!queue.isEmpty()) {
			int[] c = queue.poll();
			for(int d=0; d<4; d++) {
				int nr = c[0]+dx[d];
				int nc = c[1]+dy[d];
				if(nr<0) nr+=N;
				else if(nr>=N) nr-=N;
				if(nc<0) nc+=M;
				else if(nc>=M) nc-=M;
				if(visit[nr][nc]||map[nr][nc]==0) continue;
				if(nr==high.r&&nc==high.c) return c[2]+1;
				queue.add(new int[] {nr, nc, c[2]+1});
				visit[nr][nc] = true;
			}
		}
		return 0;
	}
	
	static Top[] findTwoTop() {
		Top low = new Top(0,0,Integer.MAX_VALUE, 0);
		Top high = new Top(0,0,0,0);
		for(Top top: tops.values()) {
			if(top.p<low.p) low=top;
			else if(top.p==low.p) {
				if(top.last>low.last) low=top;
				else if(top.last==low.last) {
					if(top.r+top.c>low.r+low.c) low=top;
					else if(top.r+top.c==low.r+low.c) {
						if(top.c>low.c) low=top;
					}
				}
			}
			if(top.p>high.p) high=top;
			else if(top.p==high.p) {
				if(top.last<high.last) high=top;
				else if(top.last==high.last) {
					if(top.r+top.c<high.r+high.c) high=top;
					else if(top.r+top.c==high.r+high.c) {
						if(top.c<high.c) high=top;
					}
				}
			}
			
		}
		Top[] lowHigh = new Top[] {low, high};
		return lowHigh;
	}

}
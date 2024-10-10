import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[][] map;
	static int er,ec;
	static HashMap<Integer, int[]> participants = new HashMap<>();
	
	static int answer = 0;
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			participants.put(i, new int[] {r,c});
		}
		st = new StringTokenizer(br.readLine());
		er = Integer.parseInt(st.nextToken())-1;
		ec = Integer.parseInt(st.nextToken())-1;
		
	
		for(int k=0; k<K; k++) {

			
			//모든 참가자 이동
			for(int i=0; i<M; i++) {
				if(!participants.containsKey(i)) continue;
				int[] rc = moveParticipant(participants.get(i));
				if(rc[0]==er&&rc[1]==ec) participants.remove(i);
				else participants.put(i, rc);
			}
					
			//회전
			if(participants.size()==0) break;
			//네모 구하기
			int[] squre = findCloseParticipant();
			//네모 돌리기(벽)
			turn(squre[0], squre[1], squre[2]);
			//사람 돌리기
			turnParticipant(squre[0], squre[1], squre[2]);
			//출구 돌리기
			turnExit(squre[0], squre[1], squre[2]);

		}
		System.out.println(answer);
		System.out.printf("%d %d", er+1, ec+1);
		
	}
	static void turnExit(int r, int c, int s) {
		int dr = er-r;
		int dc = ec-c;
		er = r + dc;
		ec = c+s-dr;
	}
	
	static void turnParticipant(int r, int c, int s) {
		for(int[] p: participants.values()) {
			if(r<=p[0] && p[0]<=r+s && c<=p[1] && p[1]<=c+s) { //사각형 내의 참가자면
				int dr = p[0]-r;
				int dc = p[1]-c;
				p[0] = r + dc;
				p[1] = c+s-dr;
			}
		}
	}
	
	static void turn(int r, int c, int s) {
		Queue<Integer> queue = new LinkedList<>();
		for(int i=r; i<=r+s; i++) {
			for(int j=c; j<=c+s; j++) {
				queue.add(map[i][j]);
			}
		}
		for(int j=c+s; j>=c; j--) {
			for(int i=r; i<=r+s; i++) {
				int n = queue.poll();
				if(n>0) n--;
				map[i][j]=n;
			}
		}
	}
	
	static int[] findSquare(int[] rc) {
		int s = Integer.max(Math.abs(rc[0]-er), Math.abs(rc[1]-ec)); //네모 크기
		int x,y;
		
		if(er<=rc[0]) x = rc[0]-s;
		else x = er-s;
		
		if(ec<=rc[1]) y = rc[1]-s;
		else y = ec-s;
		
		if(x<0) x=0;
		if(y<0) y=0;
		
		return new int[] {x,y,s};
	}
	
	static int[] findCloseParticipant() {
		int[] minRC = new int[3];
		int minD = Integer.MAX_VALUE;
		for(int[] p: participants.values()) {
			int d = Integer.max(Math.abs(p[0]-er), Math.abs(p[1]-ec));
			if(d<minD) {
				minRC = findSquare(p);
				minD = d;
			}else if(d==minD) {
				int[] rc = findSquare(p);
				if(rc[0]<minRC[0]) {
					minRC = rc;
					minD = d;
				}else if(rc[0]==minRC[0]) {
					if(rc[1]<minRC[1]) {
						minRC = rc;
						minD = d;
					}
				}
			}
		}
		return minRC;
	}
	
	
	static int[] moveParticipant(int[] rc) {
		int idx = -1;
		double minDiff = Math.abs(rc[0]-er)+Math.abs(rc[1]-ec);
		
		for(int d=0; d<4; d++) {
			int x = rc[0]+dx[d];
			int y = rc[1]+dy[d];
			if(x<0||x>=N||y<0||y>=N||map[x][y]>0) continue;
			int diff = Math.abs(x-er)+Math.abs(y-ec);
			if(diff<minDiff) {
				minDiff = diff;
				idx = d;
			}
		}
		
		if(idx>-1) {
			rc = new int[] {rc[0]+dx[idx], rc[1]+dy[idx]};
			answer++;
		}
		return rc;
	}
	
}
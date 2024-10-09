import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int[][] map;
	static int N, rr, rc;
	static int[] answer;
	
	static ArrayList<Santa> santas = new ArrayList<>();
	
	static class Santa implements Comparable<Santa>{
		int n;
		int r;
		int c;
		int t;
		public Santa(int n, int r, int c, int t) {
			super();
			this.n = n;
			this.r = r;
			this.c = c;
			this.t = t;
		}
		@Override
		public int compareTo(Santa o) {
			return this.n-o.n;
		}
		@Override
		public String toString() {
			return "Santa [n=" + n + ", r=" + r + ", c=" + c + ", t=" + t + "]";
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		int P = Integer.parseInt(st.nextToken()); //산타 수 
		answer = new int[P];
		int C = Integer.parseInt(st.nextToken()); //루돌프 힘
		int D = Integer.parseInt(st.nextToken()); //산타 힘
		
		st = new StringTokenizer(br.readLine());
		rr = Integer.parseInt(st.nextToken())-1;
		rc = Integer.parseInt(st.nextToken())-1;
		
		
		for(int p=0; p<P; p++) {
			st = new StringTokenizer(br.readLine());
			int sn = Integer.parseInt(st.nextToken());
			int sr = Integer.parseInt(st.nextToken())-1;
			int sc = Integer.parseInt(st.nextToken())-1;
			santas.add(new Santa(sn, sr, sc, 0));
			map[sr][sc] = sn;
		}
		Collections.sort(santas);
		
		for(int m=0; m<M; m++) {
			
			//가장 까까운 산타 찾기
			Santa closeSanta = findCloseSanta();
			
			//산타에게 돌진하기
			int d = moveRudolf(closeSanta);

			//산타와 충돌
			if(checkHit()) {
				int s = map[rr][rc];
				answer[s-1] += C;
				
				//층돌한 산타 이동
				rodolfHitSanta(closeSanta,d ,C);
				closeSanta.t=2;
			}
			
			for(int i=1; i<=P; i++) { //산타 이동
				
				for(Santa santa: santas) {
					if(santa.n==i) {
						if(santa.t==0) {
							d =	moveSanta(santa);
							if(d==-1) continue;
							if(santa.r==rr&& santa.c==rc) { //루동프와 충돌
								santa.t=1;
								answer[santa.n-1]+=D;
								//충돌한 산타 이동
								rodolfHitSanta(santa,(d+4)%8, D);
							}
						}else if(santa.t>0) santa.t--;
					}
				}
				
			}
			for(Santa santa: santas) {
				answer[santa.n-1]++;
			}
		
		}
		for(int i=0; i<answer.length; i++) {
			System.out.printf("%d ", answer[i]);
		}
	}
	
	static int moveSanta(Santa s) {
		
		int minIdx=-1; 
		double minD = Math.pow(s.r-rr, 2)+Math.pow(s.c-rc, 2);
		for(int d=0; d<8; d+=2) {
			int x = s.r+rx[d];
			int y = s.c+ry[d];
			if(x<0||x>=N||y<0||y>=N) continue;
			if(map[x][y]!=0) continue;
			double diff = Math.pow(x-rr, 2)+Math.pow(y-rc, 2);
			if(diff<minD) {
				 minD = diff;
				 minIdx = d;
			}
		}
		if(minIdx!=-1) {
			map[s.r][s.c] = 0;
			s.r +=rx[minIdx];
			s.c +=ry[minIdx];
			map[s.r][s.c] = s.n;
		}
		return minIdx;
	}

	static void rodolfHitSanta(Santa s, int d, int C) {
		
		map[rr][rc] = 0;
		int x = s.r+(rx[d])*C;
		int y = s.c+(ry[d])*C;
		if(x<0 || x>=N || y<0 || y>=N) {
			santas.remove(s);
			return;
		}
		s.r=x;
		s.c=y;
		
		//상호작용
		hitedSanta(s, d);
	}
	

	static void hitedSanta(Santa s1, int d) {
		if(map[s1.r][s1.c]!=0) {
			for(Santa s2: santas) {
				if(s2.n == map[s1.r][s1.c]) {
					map[s1.r][s1.c] = s1.n;
					int x = s2.r+rx[d];
					int y = s2.c+ry[d];
					if(x<0 || x>=N || y<0 || y>=N) {
						santas.remove(s2);
						return;
					}
					s2.r = x;
					s2.c = y;
					hitedSanta(s2, d);
					return;
				}
			}
		}else {
			map[s1.r][s1.c] = s1.n;
		}
	}
	
	static boolean checkHit() {
		if(map[rr][rc]!=0) return true;
		return false;
	}
	
	
	static int[] rx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] ry = {0, 1, 1, 1, 0, -1, -1, -1};
	
	static int moveRudolf(Santa s) {
		int idx = 0;
		double minD = Integer.MAX_VALUE;
		for(int r=0; r<8; r++) {
			int x = rr+rx[r];
			int y = rc+ry[r];
			if(x<0||x>=N||y<0||y>=N) continue;
			double d = Math.pow(x-s.r, 2)+Math.pow(y-s.c, 2);
			if(d<minD) {
				idx = r;
				minD = d;
			}
		}
		rr+=rx[idx];
		rc+=ry[idx];
		return idx;
	}
	
	
	static Santa findCloseSanta() {
		Santa s = santas.get(0);
		double minD=Integer.MAX_VALUE;
		for(Santa santa: santas) {
			int sr = santa.r;
			int sc = santa.c;
			double d = Math.pow(rr-sr, 2)+Math.pow(rc-sc, 2);
			if(d<minD) {
				s = santa;
				minD = d;
			}else if(d==minD) {
				if(sr>s.r) {
					s = santa;
					minD = d;
				}else if(sr==s.r) {
					if(sc>s.c) {
						s = santa;
						minD = d;	
					}
				}
			}
		}
		return s;
	}

}
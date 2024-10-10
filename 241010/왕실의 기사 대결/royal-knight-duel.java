import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	
	static class Person{
		int r;
		int c;
		int h;
		int w;
		int k;
		public Person(int r, int c, int h, int w, int k) {
			super();
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
		}
		@Override
		public String toString() {
			return "Person [r=" + r + ", c=" + c + ", h=" + h + ", w=" + w + ", k=" + k + "]";
		}
	}
	
	static int L;
	static int[][] map;
	static int[][] position;
	static HashMap<Integer, Person> persons = new HashMap<>();
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int[] before; // 초기 체력
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		int N = Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());
		before = new int[N];
		
		map = new int[L][L];
		position = new int[L][L];
		for(int i=0; i<L; i++) {
			st =  new StringTokenizer(br.readLine());
			for(int j=0; j<L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int n=1; n<=N; n++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			persons.put(n, new Person(r, c, h, w, k));
			before[n-1] = k;
			for(int i=r; i<r+h; i++) {
				for(int j=c; j<c+w; j++) {
					position[i][j] = n;
				}
			}
		}
		
		for(int q=0; q<Q; q++) {
			st = new StringTokenizer(br.readLine());
			int i = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());

			//기사 밀기
			if(canMove(i,d)) { //공격할 수 있다면
				//공격
				push(i,d);	
			}

		}
		
		int answer = 0;
		for(int n:persons.keySet()) {
			Person p = persons.get(n);
			if(p.k>0) {
				answer+=before[n-1]-p.k;
			}
		}
		System.out.println(answer);
		
		
	}
	static void push(int n, int d) {
		Person p = persons.get(n);
		removePosition(p);
		
		if(d==0) p.r--;
		else if(d==1) p.c++;
		else if(d==2) p.r++;
		else p.c--;
		
		for(int r=p.r; r<p.r+p.h; r++) {
			for(int c=p.c; c<p.c+p.w; c++) {
				if(position[r][c]!=0) pushed(position[r][c],d); //연쇄
				position[r][c] = n;
			}
		}
	}
	
	static void pushed(int n, int d) { //밀리기
		Person p = persons.get(n);
		removePosition(p);
		
		if(d==0) p.r--;
		else if(d==1) p.c++;
		else if(d==2) p.r++;
		else p.c--;
		
		int sum = 0;
		for(int r=p.r; r<p.r+p.h; r++) {
			for(int c=p.c; c<p.c+p.w; c++) {
				if(position[r][c]!=0) pushed(position[r][c],d);
				position[r][c] = n;
				if(map[r][c]==1) sum++;
			}
		}
		p.k-=sum;
		if(p.k<=0) removePosition(p);
		
	}

	
	static void removePosition(Person p) {
		for(int r=p.r; r<p.r+p.h; r++) {
			for(int c=p.c; c<p.c+p.w; c++) {
				position[r][c] = 0;
			}
		}
	}
	
	static boolean canMove(int n, int d) {
		
		Person p = persons.get(n);
		if(d%2==0) { //위아래로 움직이면
			int r;
			if(d==0) r=p.r-1; //위
			else r=p.r+p.h; //아래
			for(int c=p.c; c<p.c+p.w; c++) {
				if(r<0 || r>=L || c<0 || c>=L || map[r][c]==2) return false;
				if(position[r][c]!=0) {
					if(!canMove(position[r][c],d)) return false;
				}
			}
		}else { //좌우로 움직이면
			int c;
			if(d==1) c=p.c+p.w; //우
			else c=p.c-1; //좌
			for(int r=p.r; r<p.r+p.h; r++) {
				if(r<0 || r>=L || c<0 || c>=L || map[r][c]==2) return false;
				if(position[r][c]!=0) {
					if(!canMove(position[r][c],d)) return false;
				}
			}
		}
		return true;
	}
	
}
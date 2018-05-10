package tools;

public class Pair {
	//x
	private int key1;
	//y
	private int key2;
	public Pair(int key1, int key2){
		this.key1 = key1;
		this.key2 = key2;
	}
	public int getKey1() {
		return key1;
	}
	public void setKey1(int key1) {
		this.key1 = key1;
	}
	public int getKey2() {
		return key2;
	}
	public void setKey2(int key2) {
		this.key2 = key2;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key1;
		result = prime * result + key2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (key1 != other.key1)
			return false;
		if (key2 != other.key2)
			return false;
		return true;
	}
	
}

package ufset;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class HavingLockfreeUFSet  implements UFSet{
  private final AtomicIntegerArray parents;
  private final AtomicIntegerArray ranks;
  private final int size;
  
  public HavingLockfreeUFSet(int count) {
	  this.size = count;
    this.parents = new AtomicIntegerArray(count);
    this.ranks = new AtomicIntegerArray(count);
    for (int i = 0; i < count; i++) {
      this.parents.set(i, i);
      this.ranks.set(i, 1);
    }
  }
  
  public int getSize() {
	  return size;
  }

  public int find(int x) {
    int start = x;
    while (true) {
      
      int p = this.parents.get(x);
      this.parents.set(x, this.parents.get(p));
      if (x == p)
        break;
      x = p;
      
    }
    while (start != x)
      start = this.parents.getAndSet(start, x);
    return x;
  }

  public boolean isSame(int x, int y) {
    while (true) {
      x = this.find(x);
      y = this.find(y);
      if (x == y)
        return true;
      if (this.parents.get(x) == x)
        return false;
    }
  }

  private boolean link(int x, int y) {
	  if (this.ranks.get(x) < this.ranks.get(y))
		  return this.parents.compareAndSet(x, x, y);
	  else if (this.ranks.get(x) > this.ranks.get(y))
		  return this.parents.compareAndSet(y, y, x);
	  else {
		  if (this.parents.compareAndSet(x, x, y)) {
			  this.ranks.incrementAndGet(x);
			  return true;
		  }
		  else
			  return false;
	  }
		  
		  
  }

  public void union(int x, int y) {
    do {
      do {
        x = this.find(x);
        y = this.find(y);
        if (x == y)
          return;
      } while (!(x == this.parents.get(x) && y == this.parents.get(y)));
    } while (!this.link(x, y));
  }
}
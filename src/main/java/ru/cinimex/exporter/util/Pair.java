package ru.cinimex.exporter.util;

public class Pair<F, S> {

  private final F first;
  private final S second;

  public Pair(F first, S second) {
   if(first == null){
     throw new NullPointerException("First argument must not be null!");
   }

    if(second == null){
      throw new NullPointerException("Second argument must not be null!");
    }

    this.first = first;
    this.second = second;
  }

  public F getFirst() {
    return first;
  }

  public S getSecond() {
    return second;
  }

  @Override
  public int hashCode() {
    return first.hashCode() ^ second.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair pairo = (Pair) o;
    return this.first.equals(pairo.getFirst()) &&
        this.second.equals(pairo.getSecond());
  }

}
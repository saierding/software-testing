package swen90006.passbook;

/**
 * A pair of objects.
 */
public  class Pair<X, Y>
{
    private X first;
    private Y second;
    
    public Pair(X first, Y second)
    {
	this.first = first;
	this.second = second;
    }

    public X getFirst()
    {
	return this.first;
    }

    public Y getSecond()
    {
	return this.second;
    }
    
    public boolean equals(Pair<X, Y> other)
    {
	return first.equals(other.first) && second.equals(other.second);
    }
}

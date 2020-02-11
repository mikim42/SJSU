import java.util.*;

public class twoStack {
    private int array[];
    private int indexA;
    private int indexB;
    private int size;
    private int sizeA;
    private int sizeB;

    public twoStack(int n) {
        size = n;
        sizeA = 0;
        sizeB = 0;
        array = new int[size];
        indexA = -1;
        indexB = size;
    }

    public boolean isEmptyA() {
        return (indexA == -1);
    }

    public boolean isEmptyB() {
        return (indexB == size);
    }

    public int sizeofA() {
        return (sizeA);
    }

    public int sizeofB() {
        return (sizeB);
    }

    public void pushA(int n) {
        if (indexA == indexB - 1)
            throw new StackOverflowError("Stack overflow");
        array[++indexA] = n;
        sizeA++;
    }

    public void pushB(int n) {
        if (indexB == indexA + 1)
            throw new StackOverflowError("Stack overflow");
        array[--indexB] = n;
        sizeB++;
    }

    public void popA(int n) {
        if (indexA == -1)
            throw new StackOverflowError("Stack underflow");
        sizeA--;
        indexA--;
    }

    public void popB(int n) {
        if (indexB == size)
            throw new StackOverflowError("Stack underflow");
        sizeB--;
        indexB--;
    }

    public int peekA() {
        if (indexA == -1)
            throw new NoSuchElementException("No element");
        return array[indexA];
    }

    public int peekB() {
        if (indexB == size)
            throw new NoSuchElementException("No element");
        return array[indexB];
    }
}
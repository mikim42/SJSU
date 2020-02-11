public void reverseList(LinkedList L) {
    LinkedList iter = L.head, prev = null, next = null;

    while (iter != null) {
        next = iter.next;
        iter.next = prev;
        prev = iter;
        iter = next;
    }
    L.head = prev;
}
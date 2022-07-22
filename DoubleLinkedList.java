package edu.miracosta.cs113;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<E> extends AbstractSequentialList<E>
{  // Data fields
    private Node<E> head = null;   // points to the head of the list
    private Node<E> tail = null;   //points to the tail of the list
    private int size = 0;    // the number of items in the list

    @Override
    public void add(int index, E obj)
    {
        listIterator(index).add(obj);
    }

    public void addFirst(E obj) {
       add(0, obj);
    }

    public void addLast(E obj) {
        add(size, obj);
    }

    public E get(int index)
    {
        if (isEmpty() || index >= size || index < 0) throw new IndexOutOfBoundsException();
        return (E)listIterator(index).next();
    }
    public E getFirst() { return head.data;  }
    public E getLast() { return tail.data;  }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty(){
        return this.size() == 0;
    }

    public E remove(int index)
    {   E returnValue = null;
        ListIterator<E> iter = listIterator(index);
        if (iter.hasNext())
        {   returnValue = iter.next();
            iter.remove();
            size--;
        }
        else {   throw new IndexOutOfBoundsException();  }
        return returnValue;
    }

    @Override
    public boolean remove(Object o){
        E obj = (E)o;
        if (!contains(obj)) return false;
        else remove(indexOf(obj));
        return true;

    }

    @Override
    public void clear(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Iterator iterator() { return new ListIter(0);  }
    public ListIterator listIterator() { return new ListIter(0);  }
    public ListIterator listIterator(int index){return new ListIter(index);}
    public ListIterator listIterator(ListIterator iter)
    {     return new ListIter( (ListIter) iter);  }

    // Inner Classes
    private static class Node<E>
    {   private E data;
        private Node<E> next = null;
        private Node<E> prev = null;

        private Node(E dataItem)  //constructor
        {   data = dataItem;   }
    }  // end class Node

    public class ListIter implements ListIterator<E>
    {
        private Node<E> nextItem;      // the current node
        private Node<E> lastItemReturned;   // the previous node
        private int index = 0;   //

        public ListIter(int i)  // constructor for ListIter class
        {   if (i < 0 || i > size)
        {     throw new IndexOutOfBoundsException("Invalid index " + i); }
            lastItemReturned = null;

            if (i == size)     // Special case of last item
            {     index = size;
                nextItem = null;
            }
            else          // start at the beginning
            {   nextItem = head;
                for (index = 0; index < i; index++)  {
                    nextItem = nextItem.next;
                }
            }// end else
        }  // end constructor

        public ListIter(ListIter other)
        {   nextItem = other.nextItem;
            index = other.index;    }

        public boolean hasNext() {   return nextItem != null;    }

        public boolean hasPrevious()
        {   if (isEmpty()) return false;
            return (nextItem == null && size != 0) || nextItem.prev != null;   }

        public int previousIndex() {
            return index - 1;

        } // Fill Here
        public int nextIndex() {
            return index;
        } // Fill here
        public void set(E o)  {
            if (lastItemReturned == null) throw new IllegalStateException();
            lastItemReturned.data = o;
        }  // not implemented
        public void remove(){
            if (lastItemReturned != null) {
                if (lastItemReturned.next != null) {
                    lastItemReturned.next.prev = lastItemReturned.prev;
                } else {
                    tail = lastItemReturned.prev;
                    if (tail == null) head = null;
                    else tail.next = null;
                }

                if (lastItemReturned.prev != null)
                    lastItemReturned.prev.next = lastItemReturned.next;
                else {
                    head = lastItemReturned.next;
                    if (head == null)
                        tail = null;
                    else
                        head.prev = null;
                }
                lastItemReturned = null;
                size--;
                index--;
            }
            else throw new IllegalStateException();
        }

        public E next()
        {
            if (!hasNext()){ throw new NoSuchElementException();}
            lastItemReturned = nextItem;
            nextItem = nextItem.next;
            index++;
            return lastItemReturned.data;

        }


        public E previous()
        {
            if (!hasPrevious())
                throw new NoSuchElementException();
            if (nextItem == null)
                nextItem = tail;
            else
                nextItem = nextItem.prev;
            lastItemReturned = nextItem;
            index--;
            return lastItemReturned.data;
        }


        public void add(E obj) {
            if (head == null) { // Add to an empty list.
                head = new Node<>(obj);
                tail = head;
            } else if (nextItem == head) { // Insert at head. // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link it to the nextItem.
                newNode.next = nextItem; // Step 1 // Link nextItem to the new node.
                nextItem.prev = newNode; // Step 2 // The new node is now the head.
                head = newNode; // Step 3
            } else if (nextItem == null) { // Insert at tail. // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link the tail to the new node.
                tail.next = newNode; // Step 1
                // Link the new node to the tail.
                newNode.prev = tail; // Step 2
                // The new node is the new tail.
                tail = newNode; // Step 3
            } else { // Insert into the middle.
                // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link it to nextItem.prev.
                newNode.prev = nextItem.prev; // Step 1
                nextItem.prev.next = newNode; // Step 2
                // Link it to the nextItem.
                newNode.next = nextItem; // Step 3
                nextItem.prev = newNode; // Step 4
            }
            size++;
            index++;
            lastItemReturned = null;
        } // End of method add.


        }// end of inner class ListIter
}// end of class DoubleLinkedList
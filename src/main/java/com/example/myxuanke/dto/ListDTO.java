package com.example.myxuanke.dto;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListDTO<T> {

    private List<T> list;
    private int number;
    private int size;
    //总页数
    private long totalPages;

    //jackson的反序列化需要无参构造函数
    public ListDTO() {
        this.number = 0;
        this.size = 6;
        this.totalPages = 0;
        this.list = new List<T>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<T> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T1> T1[] toArray(T1[] a) {
                return null;
            }

            @Override
            public boolean add(T t) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends T> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public T get(int index) {
                return null;
            }

            @Override
            public T set(int index, T element) {
                return null;
            }

            @Override
            public void add(int index, T element) {

            }

            @Override
            public T remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<T> listIterator() {
                return null;
            }

            @Override
            public ListIterator<T> listIterator(int index) {
                return null;
            }

            @Override
            public List<T> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
    }

    public ListDTO(List<T> list, int number, int size, long totalPages) {
        this.list = list;
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public void add(ListDTO<T> newList){
        for(T i:newList.getList()){
            this.list.add(i);
        }
        int length = this.list.size();
        this.totalPages = Integer.valueOf(length/this.size);
        this.number = newList.getNumber();
    }
}

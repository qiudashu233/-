package com.example.myxuanke.dto;

import java.util.List;

public class ListDTO<T> {

    private List<T> list;
    private int number;
    private int size;
    //总页数
    private long totalPages;

    //jackson的反序列化需要无参构造函数
    public ListDTO() {
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
        this.totalPages = length/this.size;
    }
}

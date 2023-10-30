package ru.vsu.cs.tulitskayte;

public interface IColumn<T> {
    ICell<T> cell(int rowIndex);
    ICell<T> cell(String columnName);
    void remove();
    IColumn<T> addPrev();
    IColumn<T> addNext();
    int getIndex();
}

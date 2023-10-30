package ru.vsu.cs.tulitskayte;


public interface IRow<T> {
    ICell<T> cell(String columnName);
    ICell<T> cell(int index);
    void remove();
    IRow<T> addBefore();
    IRow<T> addAfter();
    IRow<T> prevRow();
    IRow<T> nextRow();
    int getIndex();
    int columnCount();
}

package ru.vsu.cs.tulitskayte;

public interface ICell<T> {
    T getValue();
    void setValue(T value);

    int getColumnIndex();

    int getRowIndex();

    String getColumnName();

    IColumn<T> column();

    IRow<T> row();

    ICell<T> getAbove();

    ICell<T> getBelow();

    ICell<T> getPrev();

    ICell<T> getNext();
}

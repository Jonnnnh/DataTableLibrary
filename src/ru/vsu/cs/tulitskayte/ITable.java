package ru.vsu.cs.tulitskayte;

import java.util.Iterator;

public interface ITable<T> {
    IRow<T> row(int rowIndex);

    IColumn<T> column(String columnName);

    IColumn<T> column(int columnIndex);

    ICell<T> cell(String columnName, int rowIndex);

    ICell<T> cell(int rowIndex, String columnName);

    ICell<T> cell(int rowIndex, int columnIndex);

    int rowCount();

    int columnCount();

    int getColumnIndex(String columnName);

    String getColumnName(int index);
    void remove(int index);
    void add();
    boolean isEmpty();

}

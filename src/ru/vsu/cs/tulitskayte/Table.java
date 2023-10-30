package ru.vsu.cs.tulitskayte;

import java.util.*;


public class Table<T> implements ITable<T> {
    private final List<List<T>> data = new ArrayList<>();
    private final Map<String, Integer> columnIndices = new LinkedHashMap<>();
    private final List<String> columnNames = new ArrayList<>();
    private boolean isValid = true;

    private void validateRowState() {
        if (!isValid) {
            throw new IllegalStateException("Operation is not allowed on removed row.");
        }
    }
    private void validateColumnState() {
        if (!isValid) {
            throw new IllegalStateException("Operation is not allowed on removed column.");
        }
    }

    @Override
    public IRow<T> row(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return new Row(rowIndex);
        }
        return null;
    }

    @Override
    public IColumn<T> column(String columnName) {
        Integer columnIndex = columnIndices.get(columnName);
        if (columnIndex != null) {
            return new Column(columnIndex);
        }
        return null;
    }

    @Override
    public IColumn<T> column(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < data.size()) {
            return new Column(columnIndex);
        }
        return null;
    }

    @Override
    public ICell<T> cell(String columnName, int rowIndex) {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1 || rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }
        return new Cell(rowIndex, columnIndex);
    }

    @Override
    public ICell<T> cell(int rowIndex, String columnName) {
        return cell(columnName, rowIndex);
    }

    @Override
    public ICell<T> cell(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < data.size() && columnIndex >= 0 && columnIndex < (data.isEmpty() ? 0 : data.get(0).size())) {
            return new Cell(rowIndex, columnIndex);
        }
        return null;
    }

    @Override
    public int rowCount() {
        return data.size();
    }

    @Override
    public int columnCount() {
        return data.isEmpty() ? 0 : data.get(0).size();
    }

    @Override
    public int getColumnIndex(String columnName) {
        return columnIndices.getOrDefault(columnName, -1);
    }

    @Override
    public String getColumnName(int index) {
        return index >= 0 && index < columnNames.size() ? columnNames.get(index) : null;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public void remove(int index) {
        if (index == -1) {
            index = columnCount() - 1;
        }
        if (index >= 0 && index < columnCount()) {
            for (List<T> row : data) {
                row.remove(index);
            }
            String removedName = columnNames.remove(index);
            columnIndices.remove(removedName);
            for (int i = index; i < columnNames.size(); i++) {
                columnIndices.put(columnNames.get(i), i);
            }
        }
        data.removeIf(List::isEmpty);
    }

    @Override
    public void add() {
        if (data.isEmpty()) {
            columnNames.add(null);
            columnIndices.put(null, 0);
        }
        List<T> newRow = new ArrayList<>();
        for (int i = 0; i <= columnCount(); i++) {
            newRow.add(null);
        }
        data.add(newRow);
    }

    private class Cell implements ICell<T> {
        private final int rowIndex;
        private final int columnIndex;

        public Cell(int rowIndex, int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        @Override
        public T getValue() { // напрямую получали
            return Table.this.data.get(rowIndex).get(columnIndex);
        }

        @Override
        public void setValue(T value) {
            Table.this.data.get(rowIndex).set(columnIndex, value);
        }

        @Override
        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public int getRowIndex() {
            return rowIndex;
        }

        @Override
        public String getColumnName() {
            return Table.this.getColumnName(columnIndex);
        }

        @Override
        public IColumn<T> column() {
            return Table.this.column(columnIndex);
        }

        @Override
        public IRow<T> row() {
            return Table.this.row(rowIndex);
        }

        @Override
        public ICell<T> getAbove() {
            if (rowIndex > 0) {
                return Table.this.cell(rowIndex - 1, columnIndex);
            }
            return null;
        }

        @Override
        public ICell<T> getBelow() {
            if (rowIndex < Table.this.rowCount() - 1) {
                return Table.this.cell(rowIndex + 1, columnIndex);
            }
            return null;
        }

        @Override
        public ICell<T> getPrev() {
            if (columnIndex > 0) {
                return Table.this.cell(rowIndex, columnIndex - 1);
            }
            return null;
        }

        @Override
        public ICell<T> getNext() {
            if (columnIndex < Table.this.columnCount() - 1) {
                return Table.this.cell(rowIndex, columnIndex + 1);
            }
            return null;
        }
    }

    private class Row implements IRow<T> {
        private final int rowNumber;

        public Row(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        @Override
        public ICell<T> cell(String columnName) {
            int columnIndex = Table.this.getColumnIndex(columnName);
            return Table.this.cell(rowNumber, columnIndex);
        }

        @Override
        public ICell<T> cell(int index) {
            return Table.this.cell(rowNumber, index);
        }

        @Override
        public void remove() {
            validateRowState();
            data.remove(rowNumber);
            isValid = false;
        }

        @Override
        public IRow<T> addBefore() {
            List<T> newRow = new ArrayList<>();
            for (int i = 0; i < Table.this.columnCount(); i++) {
                newRow.add(null);
            }
            Table.this.data.add(rowNumber, newRow);
            return new Row(rowNumber);
        }

        @Override
        public IRow<T> addAfter() {
            List<T> newRow = new ArrayList<>();
            for (int i = 0; i < Table.this.columnCount(); i++) {
                newRow.add(null);
            }
            Table.this.data.add(rowNumber + 1, newRow);
            return new Row(rowNumber + 1);
        }

        @Override
        public IRow<T> prevRow() {
            if (rowNumber > 0) {
                return new Row(rowNumber - 1);
            }
            return null;
        }

        @Override
        public IRow<T> nextRow() {
            if (rowNumber < Table.this.rowCount() - 1) {
                return new Row(rowNumber + 1);
            }
            return null;
        }

        @Override
        public int getIndex() {
            return rowNumber;
        }

        @Override
        public int columnCount() {
            return Table.this.columnCount();
        }
    }

    private class Column implements IColumn<T> {
        private final int columnIndex;

        public Column(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public ICell<T> cell(int rowIndex) {
            return Table.this.cell(rowIndex, columnIndex);
        }

        @Override
        public ICell<T> cell(String columnName) {
            return null;
        }

        @Override
        public void remove() {
            validateColumnState();
            for (List<T> row : data) {
                row.remove(columnIndex);
            }
            columnNames.remove(columnIndex);
            for (int i = columnIndex; i < columnNames.size(); i++) {
                columnIndices.put(columnNames.get(i), i);
            }
            isValid = false;
        }

        @Override
        public IColumn<T> addPrev() {
            for (List<T> row : data) {
                row.add(columnIndex, null);
            }
            columnNames.add(columnIndex, null);
            for (int i = columnIndex; i < columnNames.size(); i++) {
                columnIndices.put(columnNames.get(i), i);
            }
            return new Column(columnIndex);
        }

        @Override
        public IColumn<T> addNext() {
            for (List<T> row : data) {
                row.add(columnIndex + 1, null);
            }
            columnNames.add(columnIndex + 1, null);
            for (int i = columnIndex + 1; i < columnNames.size(); i++) {
                columnIndices.put(columnNames.get(i), i);
            }
            return new Column(columnIndex + 1);
        }

        @Override
        public int getIndex() {
            return columnIndex;
        }
    }
}
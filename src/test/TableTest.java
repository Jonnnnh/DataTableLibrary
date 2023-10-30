package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.tulitskayte.ICell;
import ru.vsu.cs.tulitskayte.IColumn;
import ru.vsu.cs.tulitskayte.IRow;
import ru.vsu.cs.tulitskayte.Table;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

    private static Table<String> table;

    @Nested
    class TableTests {

        @BeforeEach
        void setUp() {
            table = new Table<>();
        }
        
        @Test
        void testRow() {
            assertNull(table.row(0));
        }

        @Test
        void testColumn() {
            assertNull(table.column("test"));
        }

        @Test
        void testCellUsingColumnAndRowIndices() {
            assertNull(table.cell(0, 0));
        }

        @Test
        void testCellUsingColumnNameAndRowIndex() {
            assertNull(table.cell("test", 0));
        }

        @Test
        void testRowCount() {
            assertEquals(0, table.rowCount());
        }

        @Test
        void testColumnCount() {
            assertEquals(0, table.columnCount());
        }

        @Test
        void testGetColumnIndex() {
            assertEquals(-1, table.getColumnIndex("test"));
        }

        @Test
        void testGetColumnName() {
            assertNull(table.getColumnName(0));
        }

        @Test
        void testIsEmpty() {
            assertNull(table.getColumnName(0));
        }

        @Test
        void testRemove() {
            table.add();
            assertFalse(table.isEmpty());
            table.remove(0);
            assertTrue(table.isEmpty());
        }

        @Test
        void testAdd() {
            assertTrue(table.isEmpty());
            table.add();
            assertFalse(table.isEmpty());
            assertEquals(1, table.rowCount());
            assertEquals(1, table.columnCount());
        }
        @Test
        public void testRowCountInitiallyZero() {
            assertEquals(0, table.rowCount());
        }

        @Test
        public void testColumnCountInitiallyZero() {
            assertEquals(0, table.columnCount());
        }

        @Test
        public void testAddRowIncreasesRowCount() {
            table.add();
            assertEquals(1, table.rowCount());
        }

        @Test
        public void testAddRowPreservesColumnCount() {
            table.add();
            assertEquals(1, table.columnCount());
        }

        @Test
        public void testRemoveColumn() {
            table.add();
            table.remove(0);
            assertEquals(0, table.columnCount());
        }

        @Test
        public void testCellGetValueAndSetValue() {
            table.add();
            table.add();
            ICell<String> cell = table.cell(0, 0);
            assertNotNull(cell);
            assertNull(cell.getValue());

            cell.setValue("Hello");
            assertEquals("Hello", cell.getValue());
        }
    }

    @Nested
    class AdditionalTableTests {
        @BeforeEach
        void setUp() {
            table = new Table<>();
        }

        // Cell
        @Test
        void testCellGetValue() {
            table.add();
            ICell<String> cell = table.cell(0, 0);
            assertNull(cell.getValue());
            cell.setValue("Hello");
            assertEquals("Hello", cell.getValue());
        }

        @Test
        void testCellGetAboveAndBelow() {
            table.add();
            table.add();
            ICell<String> firstCell = table.cell(0, 0);
            ICell<String> secondCell = table.cell(1, 0);
            assertNull(firstCell.getAbove());
            assertSameCell(firstCell, secondCell.getAbove());
            assertSameCell(secondCell, firstCell.getBelow());
            assertNull(secondCell.getBelow());
        }
        private void assertSameCell(ICell<String> expected, ICell<String> actual) {
            assertNotNull(actual, "Actual cell is null");
            assertEquals(expected.getRowIndex(), actual.getRowIndex(), "Row indices do not match");
            assertEquals(expected.getColumnIndex(), actual.getColumnIndex(), "Column indices do not match");
        }

        // Row
        @Test
        void testRowAddBeforeAndAfter() {
            table.add();
            IRow<String> row = table.row(0);
            row.addBefore();
            row.addAfter();
            assertEquals(3, table.rowCount());
        }

        @Test
        void testRowRemove() {
            table.add();
            IRow<String> row = table.row(0);
            row.remove();
            assertEquals(0, table.rowCount());
        }

        @Test
        void testRowPrevAndNextRow() {
            table.add();
            table.add();
            IRow<String> firstRow = table.row(0);
            IRow<String> secondRow = table.row(1);

            assertNull(firstRow.prevRow());
            assertSameRow(secondRow, firstRow.nextRow());

            assertSameRow(firstRow, secondRow.prevRow());
            assertNull(secondRow.nextRow());
        }

        private void assertSameRow(IRow<String> expected, IRow<String> actual) { // вспомогательный метод проверяет, совпадают ли индексы двух строк, если они представляют одну и ту же строку в таблице
            assertEquals(expected.getIndex(), actual.getIndex(), "Rows are not the same.");
        }

        // Column
        @Test
        void testColumnAddPrevAndNext() {
            table.add();
            IColumn<String> column = table.column(0);
            column.addPrev();
            column.addNext();
            assertEquals(3, table.columnCount());
        }

        @Test
        void testColumnRemove() {
            table.add();
            IColumn<String> column = table.column(0);
            column.remove();
            assertEquals(0, table.columnCount());
        }
    }
}
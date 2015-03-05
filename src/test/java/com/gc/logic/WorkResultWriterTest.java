package com.gc.logic;

import com.gc.app.Settings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkResultWriterTest {

    private static final DateTime NOW = new DateTime(2014, 10, 22, 12, 58, 57);

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(NOW.getMillis());
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void test() throws Exception {
        final Settings settings = mock(Settings.class);
        when(settings.getRegime()).thenReturn("write");
        when(settings.getWriteInterval()).thenReturn(100);
        when(settings.getStartFrom()).thenReturn(1);
        when(settings.getSteps()).thenReturn(10);
        when(settings.getExpire()).thenReturn(100);
        final LogicSettings sets = new LogicSettings(settings);

        final WorkResult.Builder builder = WorkResult.builder(sets);
        builder.notWritten(1);
        builder.notWritten(2);
        builder.written(3);
        builder.notRead(4);
        builder.readNotSame(5);
        builder.readNotSame(6);
        builder.readNotSame(7);
        builder.readSame(8);
        builder.readSame(9);
        builder.readSame(10);
        builder.readSame(11);
        final List<String> strings = WorkResultWriter.constructReport(builder.build());
        final Iterator<String> s = strings.iterator();
        assertThat(s.next(), is("--------- Время 2014-10-22 12:58:57"));
        assertThat(s.next(), is("--------- От 1 До 10 Кол-во шагов 10"));
        assertThat(s.next(), is("--- Режим WRITE"));
        assertThat(s.next(), is("Не записалось (2 штук): [1, 2]"));
        assertThat(s.next(), is("Записалось (1 штук): [3]"));
        assertThat(s.next(), is("--- Режим READ"));
        assertThat(s.next(), is("Не прочиталось (1 штук): [4]"));
        assertThat(s.next(), is("Прочиталось не верно (3 штук): [5, 6, 7]"));
        assertThat(s.next(), is("Прочиталось (4 штук): [8, 9, 10, 11]"));
        assertThat(s.next(), is(""));
    }
}
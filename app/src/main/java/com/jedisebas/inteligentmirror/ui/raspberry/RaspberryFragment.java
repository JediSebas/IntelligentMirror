package com.jedisebas.inteligentmirror.ui.raspberry;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedisebas.inteligentmirror.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.sql.SQLOutput;

/**
 * Class that show resource usage of mirror.
 */

public class RaspberryFragment extends Fragment {

    private RaspberryViewModel raspberryViewModel;
    GraphView line_graph;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        raspberryViewModel =
                new ViewModelProvider(this).get(RaspberryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_raspberry, container, false);
        line_graph = root.findViewById(R.id.line_graph);
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
     //   line_graph.addSeries(lineSeries);
        LineGraphSeries<DataPoint> nextLines = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(2, -1),
                new DataPoint(3, 5),
                new DataPoint(4, 6),
                new DataPoint(5, 21),
                new DataPoint(6, 5),
                new DataPoint(7, 1),
                new DataPoint(8,3)
        });
        nextLines.setColor(Color.RED);
     //   line_graph.addSeries(nextLines);

        RaspberryGraph raspberry = new RaspberryGraph();
        raspberry.setY(-10);
        Iterate i = new Iterate(raspberry);
        Draw d = new Draw(raspberry);

        i.t.start();
        d.t.start();

        return root;
    }

    private class RaspberryGraph{

        int x=1, y;
        boolean end = false;

        RaspberryGraph() {
        }

        void endGraph() {
            end = true;
        }

        void setY(int y) {
            this.y = y;
        }

        synchronized void iterate() {
            if (y>0) {
                for (;y>0; y--) {
                    try {
                        wait(1000);
                        notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (;y<0; y++) {
                    try {
                        wait(1000);
                        notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                endGraph();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized void draw() {
            System.out.println("-----------WYBUDZONY NA PCOZATKU-----------");
            DataPoint d = new DataPoint(x, y);
            System.out.println("-----------Y w d-----------" + d.getY());
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(d.getX()-1, 0),
                    d
            });
            line_graph.addSeries(series);
            x++;
            try {
                System.out.println("-----------SPI POD KONIEC-----------");
                wait(1000);
                notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-----------WYBUDZONY POD KONIEC-----------");
            if (end) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Iterate implements Runnable {

        RaspberryGraph R;
        Thread t;

        Iterate(RaspberryGraph R) {
            t = new Thread(this);
            this.R = R;
        }

        @Override
        public void run() {
            while (true) {
                R.iterate();
            }
        }
    }

    private class Draw implements Runnable {

        RaspberryGraph R;
        Thread t;

        Draw(RaspberryGraph R) {
            t = new Thread(this);
            this.R = R;
        }

        @Override
        public void run() {
            while (true) {
                R.draw();
            }
        }
    }
}
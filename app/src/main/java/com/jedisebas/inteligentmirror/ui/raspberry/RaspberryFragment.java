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
        raspberry.setY(20);

        new Thread(raspberry::draw).start();
        new Thread(raspberry::iterate).start();

        return root;
    }

    private class RaspberryGraph{

        private int y;
        private DataPoint[] pointTable = new DataPoint[9];

        RaspberryGraph() {
            for (int i=0; i<=8; i++) {
                pointTable[i] = new DataPoint(0, 0);
            }
        }

        void setY(int y) {
            this.y = y;
        }

        void setHeight() {
            LineGraphSeries<DataPoint> nothing = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, 20)
            });
            line_graph.addSeries(nothing);
        }

        void levelUpTable() {
            line_graph.removeAllSeries();
            for (int i=0; i<8; i++) {
                pointTable[i] = new DataPoint(i, pointTable[i+1].getY());
            }
            LineGraphSeries<DataPoint> rePaint = new LineGraphSeries<>(pointTable);
            line_graph.addSeries(rePaint);
            setHeight();
        }

        synchronized void draw() {
            for (int x = 1; x <= 8; x++) {
                setHeight();
                DataPoint d = new DataPoint(x, y);
                pointTable[0] = new DataPoint(0, d.getY());
                pointTable[x] = d;
                System.out.println("-----------Y w d-----------" + d.getY());

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        pointTable[x-1],
                        pointTable[x]
                });

                line_graph.addSeries(series);

                notify();
                try {
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                levelUpTable();
                DataPoint d = new DataPoint(8, y);
                pointTable[8] = d;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointTable);
                notify();
                try {
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        synchronized void iterate() {
            while (true) {
                if (y > 0) {
                    y--;
                } else {
                    y++;
                }
                notify();
                try {
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
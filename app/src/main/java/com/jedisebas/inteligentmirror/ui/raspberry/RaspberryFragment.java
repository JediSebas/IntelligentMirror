package com.jedisebas.inteligentmirror.ui.raspberry;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedisebas.inteligentmirror.ConnectionData;
import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that show resource usage of mirror.
 */

public class RaspberryFragment extends Fragment {

    private RaspberryViewModel raspberryViewModel;
    GraphView line_graph, ram, temp, wifi;
    GraphView[] graphArray = new GraphView[4];

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        raspberryViewModel =
                new ViewModelProvider(this).get(RaspberryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_raspberry, container, false);
        graphArray[0] = line_graph = root.findViewById(R.id.line_graph_cpu);
        graphArray[1] = ram = root.findViewById(R.id.line_graph_ram);
        graphArray[2] = temp = root.findViewById(R.id.line_graph_temp);
        graphArray[3] = wifi = root.findViewById(R.id.line_graph_wifi);

        for (GraphView v : graphArray) {
            v.getViewport().setYAxisBoundsManual(true);
            v.getViewport().setMaxY(100);
        }

        RaspberryGraph raspberry = new RaspberryGraph();

        new Thread(raspberry::draw).start();
        new Thread(raspberry::iterate).start();

        return root;
    }

    private class RaspberryGraph{

        private int y, y_ram, y_temp, y_wifi;

        private DataPoint[] pointTable = new DataPoint[9];
        private DataPoint[] pointTable_ram = new DataPoint[9];
        private DataPoint[] pointTable_temp = new DataPoint[9];
        private DataPoint[] pointTable_wifi = new DataPoint[9];
        private DataPoint[][] pointArray = new DataPoint[4][];

        RaspberryGraph() {
            pointArray[0] = pointTable;
            pointArray[1] = pointTable_ram;
            pointArray[2] = pointTable_temp;
            pointArray[3] = pointTable_wifi;
            for (DataPoint[] arr: pointArray) {
                for (int i=0; i<=8; i++) {
                    arr[i] = new DataPoint(i, 0);
                }
            }
        }

        void setY(int y) {
            this.y = y;
        }

        void setY_ram(int y) {
            this.y_ram = y;
        }

        void setY_temp(int y) {
            this.y_temp = y;
        }

        void setY_wifi(int y) {
            this.y_wifi = y;
        }

        void levelUpTable() {
            for (int j=0; j<4; j++) {
                graphArray[j].removeAllSeries();
                for (int i = 0; i < 8; i++) {
                    pointArray[j][i] = new DataPoint(i, pointArray[j][i + 1].getY());
                }
                LineGraphSeries<DataPoint> rePaint = new LineGraphSeries<>(pointArray[j]);
                rePaint.setThickness(10);
                switch (j) {
                    case 1:
                        rePaint.setColor(Color.MAGENTA);
                        break;
                    case 2:
                        rePaint.setColor(Color.RED);
                        break;
                    case 3:
                        rePaint.setColor(Color.YELLOW);
                        break;
                }
                graphArray[j].addSeries(rePaint);
            }
        }

        synchronized void draw() {
            while (true) {
                DataPoint d = new DataPoint(8, y);
                DataPoint d_ram = new DataPoint(8, y_ram);
                DataPoint d_temp = new DataPoint(8, y_temp);
                DataPoint d_wifi = new DataPoint(8, y_wifi);
                pointTable[8] = d;
                pointTable_ram[8] = d_ram;
                pointTable_temp[8] = d_temp;
                pointTable_wifi[8] = d_wifi;
                levelUpTable();
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
                y = randint();
                y_ram = randint();
                y_temp = randint();
                y_wifi = randint();
                notify();
                try {
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int randint() {
            double x;
            x = Math.random();
            x *= 100;
            x = Math.round(x);
            return (int) x;
        }
    }

    private class GetRaspberryInfo implements Runnable {

        Thread t;

        GetRaspberryInfo() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            String QUERY = "";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("DRIVER STILL WORKS BTW");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection conn = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER, ConnectionData.PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY);

            } catch (SQLException throwables) {
                System.out.println("HERE IS SOMETHING WRONG");
                throwables.printStackTrace();
            }
        }
    }
}
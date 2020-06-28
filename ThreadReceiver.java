package sample;


import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ThreadReceiver extends Thread{

     private InputStream inputStream;
     private ArrayList<Latter> letters;
     private ListView listLater;

    public ThreadReceiver(InputStream inputStream, ArrayList<Latter> letters, ListView listLater) {
        this.inputStream = inputStream;
        this.letters = letters;
        this.listLater = listLater;
    }


    @Override
    public void run()
    {
        while (true) {

            String lineLater = "";
            byte[] later = new byte[128];
            int later_bytes = 0;
            try {
                do {
                    later_bytes = inputStream.read(later, 0, later.length);
                    lineLater += new String(later, 0, later_bytes);
                }
                while (inputStream.available() != 0);
            } catch (IOException e) {
                break;
            }
            if (!lineLater.equals("нет писем")) {
                String[] split_later = lineLater.split(";");

                for (int i = 0; i < split_later.length; i = i + 5)
                {
                    Latter latter = new Latter();
                    latter.setSender(split_later[1 + i]);
                    latter.setText(split_later[0+i]);
                    latter.setTitle(split_later[2+i]);
                    latter.setName(split_later[3+i]);
                    latter.setFamily_name(split_later[4+i]);
                    letters.add(latter);
                    listLater.getItems().add(latter.getSender() + "                           " + latter.getTitle());

                }
            }
        }

    }


}
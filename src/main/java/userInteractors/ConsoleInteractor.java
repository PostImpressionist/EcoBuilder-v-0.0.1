package userInteractors;

import channels.ChannelType;
import datafulfiller.TariffParser;
import exceptions.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConsoleInteractor {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
    public static int reserve = 0;

    public static void writeMessage(String message){
        System.out.println(message);
    }
    public static String readString() throws InterruptOperationException {
        String message ="";
        try{
            message = bis.readLine();

        }catch(IOException ignore){}

        if(message.toLowerCase().equals("exit")){
            ConsoleInteractor.writeMessage("the.end");
            throw new InterruptOperationException();
        }

        return message;
    }

    public static Map<ChannelType,Integer> askTaskInformation() throws InterruptOperationException{
        Map<ChannelType,Integer> resultMap = new HashMap<>();
        do{
            try{
                // tune the input data
                //ConsoleHelper.writeMessage("Type Analog Inputs count. AI = ");



                // Analog input
                ConsoleInteractor.writeMessage("Type Analog Inputs count. AI = ");
                resultMap.put(ChannelType.UI, Integer.parseInt(ConsoleInteractor.readString()));
                // Analog output
                ConsoleInteractor.writeMessage("Type Analog Outputs count. AO = ");
                resultMap.put(ChannelType.AO, Integer.parseInt(ConsoleInteractor.readString()));
                // Digital input
                ConsoleInteractor.writeMessage("Type Digital Inputs count. DI = ");
                resultMap.put(ChannelType.DI, Integer.parseInt(ConsoleInteractor.readString()));
                // Digital output
                ConsoleInteractor.writeMessage("Type Digital Outputs count. DO = ");
                resultMap.put(ChannelType.DO, Integer.parseInt(ConsoleInteractor.readString()));

                // Digital output
                ConsoleInteractor.writeMessage("Type in % how many to reserve = ");
                reserve = Integer.parseInt(ConsoleInteractor.readString());
                if(reserve < 0 || reserve > 100) throw new IllegalArgumentException();
                    else if(reserve != 0){
                    for (ChannelType key: resultMap.keySet()) {
                        resultMap.merge(key,(resultMap.get(key)*reserve)/100, Integer::sum);
                    }
                }

                break;
            }catch(IllegalArgumentException  ignore){
                ConsoleInteractor.writeMessage("invalid.data");
                continue;
            }
        }while(true);//end while
        return resultMap;
    }

}

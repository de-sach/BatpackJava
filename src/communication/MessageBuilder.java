/*
 * Copyright (C) 2017 sach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package communication;

/**
 *
 * @author sach
 */
public class MessageBuilder {

    private String message;

    /**
     * An object that can build correct commands to send to the battery packet
     */
    public MessageBuilder() {

    }

    /**
     * A message used to get the number of modules which can be used to create a
     * modular BatteryPacket
     *
     * @return the commando as String
     */
    public String buildBatpackMessage() {
        this.message = "M_0_0000";
        this.message = finishMessage(this.message);
        return this.message;
    }

    private String buildMessage(int type, int identifier) {
        switch (type) {
            case 0: //get all 
                this.message += "A_0000";
                break;
            case 1: //get Module 
                this.message += "M_";
//                for(int i=(int) (4-Math.floor(Math.log10(identifier)+1));i>0;i--){
//                    this.message+="0";
//                }
                this.message += identifier;
                break;
            case 2: //get cell 
                this.message += "C_";
//                for(int i=(int) (4-Math.floor(Math.log10(identifier)+1));i>0;i--){
//                    this.message+="0";
//                }
                this.message += identifier;
                break;
            default:
                System.out.println("error wrong message");
                break;
        }
        this.message = finishMessage(this.message);
        return this.message;
    }

    /**
     * A method that returns a message to get the voltage of a batterypacket,
     * module or cell
     *
     * @param type The type of voltage message that is sent, 0-2 for batpack,
     * module or cell
     * @param identifier the identifier for the module or cell
     * @return the commando as a String
     */
    public String buildVoltageMessage(int type, int identifier) {
        this.message = "V_";
        this.message = buildMessage(type, identifier);
        return this.message;
    }

    /**
     * A method that returns a message to get the temperature of a
     * batterypacket, module or cell
     *
     * @param type The type of voltage message that is sent, 0-2 for batpack,
     * module or cell
     * @param identifier the identifier for the module or cell
     * @return the commando as a String
     */
    public String buildTemperatureMessage(int type, int identifier) {
        this.message = "T_";
        this.message = buildMessage(type, identifier);
        return this.message;
    }

    /**
      * A method that returns a message to get the balancing state of a
     * batterypacket, module or cell
     *
     * @param type The type of voltage message that is sent, 0-2 for batpack,
     * module or cell
     * @param identifier the identifier for the module or cell
     * @return the commando as a String
     */
    public String buildBalancingMessage(int type, int identifier) {
        this.message = "B_";
        this.message = buildMessage(type, identifier);
        return this.message;
    }

    private String finishMessage(String message) {
        message += "\r\n";
        assert (message.length() == 10);
        return message;
    }

}

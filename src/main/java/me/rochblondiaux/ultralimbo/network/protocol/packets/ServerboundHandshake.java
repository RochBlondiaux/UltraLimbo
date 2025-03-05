/*
 * Copyright (C) 2020 Nan1t
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.rochblondiaux.ultralimbo.network.protocol.packets;


import lombok.Getter;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Getter
public class ServerboundHandshake implements ServerboundPacket {

    private Version version;
    private String host;
    private int port;
    private State nextState;

    @Override
    public void decode(ByteMessage msg, Version version) {
        try {
            this.version = Version.of(msg.readVarInt());
        } catch (IllegalArgumentException e) {
            this.version = Version.UNDEFINED;
        }

        this.host = msg.readString();
        this.port = msg.readUnsignedShort();
        this.nextState = State.getById(msg.readVarInt());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


}

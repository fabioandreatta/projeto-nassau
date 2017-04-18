package com.nassau.br.zookeeper;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import com.nassau.br.Module;

public class ModuleSerializer implements ZkSerializer {
	@Override
	public Object deserialize(byte[] topic) throws ZkMarshallingError {
		return new Module().setKafkaTopic(new String(topic));
	}

	@Override
	public byte[] serialize(Object object) throws ZkMarshallingError {
		if (object instanceof Module) {
			Module module = (Module) object;
			return module.getKafkaTopic().getBytes();
		}
		return null;
	}

}

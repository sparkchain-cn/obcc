package cn.obcc.utils;
//package com.sparkchain.chain.junction.utils;
//

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * IpfsUtils
 *
 * @author ecasona
 * @version 1.6
 * @date 2018/12/4 8:30
 * @details
 */
public class IpfsUtils {

    public static Map<String, IPFS> ipfsMap = new HashMap<>();

    public static boolean exits(String ipfsAddress) {
        return ipfsMap.containsKey(ipfsAddress);
    }

    public static void put(String ipfsAddress) {
        if (!exits(ipfsAddress)) {
            ipfsMap.put(ipfsAddress, new IPFS(ipfsAddress));
        }
    }

    public static IPFS getIpfs(String ipfsAddress) {
        put(ipfsAddress);
        return ipfsMap.get(ipfsAddress);
    }

    public static String save(String ipfsAddress, String memos) throws IOException {
        IPFS ipfs = getIpfs(ipfsAddress);

        NamedStreamable.ByteArrayWrapper byteArrayWrapper = new NamedStreamable.ByteArrayWrapper(memos.getBytes());
        MerkleNode addResult = ipfs.add(byteArrayWrapper).get(0);
        return addResult.hash.toString();
    }


    public static String save(String ipfsAddress, InputStream is) throws Exception {
        IPFS ipfs = getIpfs(ipfsAddress);

        NamedStreamable.ByteArrayWrapper byteArrayWrapper =
                new NamedStreamable.ByteArrayWrapper(FileUtils.inputStreamToBytes(is));
        MerkleNode addResult = ipfs.add(byteArrayWrapper).get(0);
        return addResult.hash.toString();
    }


    public static String cat(String ipfsAddress, String hash) throws IOException {
        IPFS ipfs = getIpfs(ipfsAddress);
        byte[] data = ipfs.cat(Multihash.fromBase58(hash));
        return new String(data);
    }


}

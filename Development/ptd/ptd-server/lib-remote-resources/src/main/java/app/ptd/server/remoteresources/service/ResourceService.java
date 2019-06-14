/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ptd.server.remoteresources.service;

import app.ptd.server.remoteresources.RemoteResourceException;
import java.net.URL;
import java.util.Optional;
import app.ptd.server.remoteresources.Resource;

/**
 *
 */
public interface ResourceService {
    
    public Optional<Resource> resource(URL url) throws RemoteResourceException;
    
}

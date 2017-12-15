/*
 * Created by Dong Gyu Lee on 2017.12.06  * 
 * Copyright © 2017 Dong Gyu Lee. All rights reserved. * 
 */
package com.mycompany.managers;
import com.mycompany.EntityBeans.Dish;
import com.mycompany.EntityBeans.User;
import com.mycompany.managers.AccountManager;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


/*
---------------------------------------------------------------------------
JSF Managed Beans annotated with @ManagedBean from javax.faces.bean
is in the process of being deprecated in favor of CDI Beans. 

You should annotate the bean class with @Named from javax.inject.Named 
package to designate the bean as managed by the
Contexts and Dependency Injection (CDI) container.

Beans annotated with @Named is the preferred approach, because CDI 
enables Java EE-wide dependency injection. 

A CDI bean is a bean managed by the CDI container. 

Within JSF facelets XHTML pages, this bean will be referenced by using the
name 'accountManager'. Actually, the default name is the class name starting
with a lower case letter and value = 'accountManager' is optional;
However, we spell it out to make our code more readable and understandable.
---------------------------------------------------------------------------
 */
@Named(value = "mapManager")

/*
The @SessionScoped annotation preserves the values of the AccountManager
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
 
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the AccountManager class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class MapManager implements Serializable {
    private MapModel mapModel;
    private Marker marker;
    private AccountManager manager = new AccountManager();
    private User currentUser;
    private String lat = "0";
    private String lng = "0";
    

    @PostConstruct
    public void init() {
        mapModel = new DefaultMapModel();

        //Shared coordinates
        LatLng coord1 = new LatLng(30, -80);
          
        //Basic marker
        mapModel.addOverlay(new Marker(coord1, "Current User"));
    }
  
    public MapModel getMapModel(User currentUser, List<Dish> dishes) {
        String location = currentUser.getLocation();
        String[] locationArray = location.split(",");

        if (locationArray.length > 1) {
            lat = locationArray[0];
            lng = locationArray[1];
            LatLng coord1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            mapModel.addOverlay(new Marker(coord1, currentUser.getUsername()));
        }
        
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            User tempUser = dish.getUserId();
            String[] userLocationArray = tempUser.getLocation().split(",");
            LatLng tempCoord;
            if (userLocationArray.length > 0) {
                tempCoord = new LatLng(Double.parseDouble(userLocationArray[0]), 
                        Double.parseDouble(userLocationArray[1]));
                mapModel.addOverlay(new Marker(tempCoord, tempUser.getUsername(), dish, 
                        "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
            }
            
        }
        return mapModel;
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
    
    public Marker getMarker() {
        return marker;
    }
}

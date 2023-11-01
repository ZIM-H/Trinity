using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class NoButton : MonoBehaviour, IPointerClickHandler
{

    // Start is called before the first frame update
    GameObject joystick; 
    GameObject joystickVision;
    GameObject interactiveUIPanel;
    void Start()
    {   
        joystick = GameObject.Find("Analog");
        joystickVision = GameObject.Find("AnalogVision");
        

    }
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("Yes");
        interactiveUIPanel = GameObject.Find("InteractiveUIPanel");
        interactiveUIPanel.SetActive(false);
        joystick.SetActive(true);
        joystickVision.SetActive(true);
    }
}

using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class InteractiveUI : MonoBehaviour, IPointerClickHandler
{
    // Start is called before the first frame update
    GameObject joystick; 
    GameObject joystickVision;
    GameObject interactiveUIPanel;
    GameObject canvas;
    GameObject noButton;
    void Start()
    {   
        canvas = GameObject.Find("Canvas");
        joystick = canvas.transform.Find("Analog").gameObject;
        joystickVision = canvas.transform.Find("AnalogVision").gameObject;
        interactiveUIPanel = gameObject;
        noButton = canvas.transform.Find("InteractiveUIPanel").GetChild(0).GetChild(0).Find("No").gameObject;
        gameObject.SetActive(false);
    }

    // Update is called once per frame
    public void OnPointerClick(PointerEventData eventData){
    if(interactiveUIPanel.name == "InteractiveUIPanel"){
        noButton.GetComponent<NoButton>().NoWay();
    }else{
    gameObject.SetActive(false);
    joystick.SetActive(true);
    joystickVision.SetActive(true);
    }

}
}

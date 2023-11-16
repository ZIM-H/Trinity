using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class TouchMemoTable : MonoBehaviour, IPointerClickHandler
{
    // Start is called before the first frame update
    GameObject canvas;
    GameObject joystick;
    GameObject joystickVision;
    GameObject interactiveUIPanelForMemo;
    public void OnPointerClick(PointerEventData eventData){
        interactiveUIPanelForMemo.SetActive(true);
        joystick.SetActive(false);
        joystickVision.SetActive(false);
    }

    void Start()
    {
        canvas = GameObject.Find("Canvas");
        joystick = canvas.transform.Find("Analog").gameObject;
        joystickVision = canvas.transform.Find("AnalogVision").gameObject;
        interactiveUIPanelForMemo = canvas.transform.Find("InteractiveUIPanelForMemo").gameObject;
    }

    // Update is called once per frame
    void Update()
    {
        
    }

}

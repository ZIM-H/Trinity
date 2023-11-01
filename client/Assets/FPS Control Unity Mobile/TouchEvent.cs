using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
public class TouchEvent : MonoBehaviour, IPointerClickHandler
{
    public string objectName;
    public bool condition = false;
    GameObject interactiveUI;
    GameObject interactiveUIPanel;
    GameObject joystick;
    GameObject joystickVision;
    GameObject interactiveCamera;
    void Start()
    {
        condition = false;
        
        joystick = GameObject.Find("Analog");
        joystickVision = GameObject.Find("AnalogVision");
        interactiveUI = GameObject.Find("InteractiveUI");
        interactiveCamera = GameObject.Find("InteractiveCameraImage");
        interactiveUIPanel = GameObject.Find("InteractiveUIPanel");
    }
    public void OnPointerClick(PointerEventData eventData){

        RenderTexture texture; 

        string PATH = "InteractiveUI/RenderTexture/Render" + objectName;    //이미지 위치를 저장하는 변수
        texture = Resources.Load(PATH, typeof(RenderTexture)) as RenderTexture;  //이미지 로드
        interactiveCamera.GetComponent<RawImage>().texture = texture;

        Texture2D templateImage;
        string PATH_template = "InteractiveUI/"+objectName;
        Debug.Log(PATH_template);
        templateImage = Resources.Load(PATH_template, typeof(Texture2D)) as Texture2D;
        Debug.Log(templateImage);
        interactiveUI.GetComponent<RawImage>().texture = templateImage;




        interactiveUIPanel.SetActive(true);
        joystick.SetActive(false);
        joystickVision.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}

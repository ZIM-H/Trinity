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
    GameObject canvas;
    void Start()
    {   
        canvas = GameObject.Find("Canvas");
        joystick = canvas.transform.Find("Analog").gameObject;
        joystickVision = canvas.transform.Find("AnalogVision").gameObject;
        interactiveUIPanel = canvas.transform.Find("InteractiveUIPanel").gameObject;
    }
    public void OnPointerClick(PointerEventData eventData){
       NoWay();
    }
    public void NoWay(){
         Debug.Log("No");
        if(VariableManager.Instance.asteroidStatus && VariableManager.Instance.blackHoleObserved &&
         canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").GetComponent<RawImage>().texture.name == "Asteroids")
         {
            RenderTexture texture; 

            string PATH = "InteractiveUI/RenderTexture/RenderBlackHole";    //이미지 위치를 저장하는 변수
            texture = Resources.Load(PATH, typeof(RenderTexture)) as RenderTexture;  //이미지 로드
            Debug.Log(texture);
            canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").GetComponent<RawImage>().texture = texture;

            Texture2D templateImage;
            string PATH_template = "InteractiveUI/BlackHole";
            Debug.Log(PATH_template);
            templateImage = Resources.Load(PATH_template, typeof(Texture2D)) as Texture2D;
            Debug.Log(templateImage);
            canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").GetComponent<RawImage>().texture = templateImage;
        }else{
            canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("Yes").GetComponent<OKButtonDisabler>().Activate();
            canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("No").GetComponent<OKButtonDisabler>().Activate();
            canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("OK").GetComponent<OKButtonDisabler>().Deactivate();
            interactiveUIPanel.SetActive(false);
            joystick.SetActive(true);
            joystickVision.SetActive(true);
        }
    }
}

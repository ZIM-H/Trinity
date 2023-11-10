using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;

using UnityEngine.UI;
public class TouchEvent : MonoBehaviour, IPointerClickHandler
{
    string objectName;
    public bool condition = false;

    GameObject canvas;
    GameObject joystick;
    GameObject joystickVision;
    void Start()
    {
        objectName = this.gameObject.name;
        condition = CanTouchThisObject(objectName);
        Debug.Log("hi "+objectName + " : " + condition);
        joystick = GameObject.Find("Analog");
        joystickVision = GameObject.Find("AnalogVision");
        canvas = GameObject.Find("Canvas");
    }
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log(condition);
        if(condition == false && VariableManager.Instance.power > 0){
        RenderTexture texture; 

        string PATH = "InteractiveUI/RenderTexture/Render" + objectName;    //이미지 위치를 저장하는 변수
        texture = Resources.Load(PATH, typeof(RenderTexture)) as RenderTexture;  //이미지 로드
        Debug.Log(texture);
        canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").GetComponent<RawImage>().texture = texture;

        Texture2D templateImage;
        string PATH_template = "InteractiveUI/"+objectName;
        Debug.Log(PATH_template);
        templateImage = Resources.Load(PATH_template, typeof(Texture2D)) as Texture2D;
        Debug.Log(templateImage);
        canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").GetComponent<RawImage>().texture = templateImage;




        canvas.transform.Find("InteractiveUIPanel").gameObject.SetActive(true);
        joystick.SetActive(false);
        joystickVision.SetActive(false);
        }else if(condition == true){
            Debug.Log("안번쩍거리게해야함");
        }else if(VariableManager.Instance.power == 0){
            Debug.Log("힘이 없음");
        }

        
    }
    public bool CanTouchThisObject(string name){
        if(name == "Water" && VariableManager.Instance.purifierStatus){
        return true;
        }else if(name == "FertilizerResearch" && VariableManager.Instance.fertilizerUpgrade){
        return true;
        }else if(name == "CO2Fix" && VariableManager.Instance.carbonCaptureStatus == 0){
        return true;
        }else if(name == "CentralPark" && VariableManager.Instance.farmStatus){
        return true;
        }else if(name == "Taurine" && VariableManager.Instance.taurineFilterTry){
        return true;
        }else if(name == "Weapon"){
        return true;
        }
        else{
        return false;
        }
    }

}
    // Update is called once per frame
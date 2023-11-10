using System.Collections;
using System.Collections.Generic;
using cakeslice;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using UnityEngine.UIElements;
using DG.Tweening;
using Unity.VisualScripting;
using TMPro;
public class YesButton : MonoBehaviour, IPointerClickHandler
{

    // Start is called before the first frame update
    GameObject powerBar;
    GameObject joystick; 
    GameObject joystickVision;
    GameObject interactiveUIPanel;
    GameObject mainCamera;
    string target;
    GameObject Target;
    GameObject canvas;
    GameObject weapon;
    GameObject fertilizerAlert;
    void Start()
    {   
        powerBar = GameObject.Find("Fill");
        mainCamera = GameObject.Find("Main Camera");
        weapon = GameObject.Find("Weapon");
        canvas = GameObject.Find("Canvas");
        fertilizerAlert = canvas.transform.Find("FertilizerAlert").gameObject;
        joystick = canvas.transform.Find("Analog").gameObject;
        joystickVision = canvas.transform.Find("AnalogVision").gameObject;

    }
    IEnumerator LackOfFertilizer(float time){
        fertilizerAlert.SetActive(true);
        yield return new WaitForSeconds(time);
        fertilizerAlert.SetActive(false);
    }
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("Yes");
        interactiveUIPanel = GameObject.Find("InteractiveUIPanel");
        target = interactiveUIPanel.transform.GetChild(0).transform.GetChild(0).gameObject.GetComponent<RawImage>().texture.name;
        Debug.Log(target);
        Target = GameObject.Find(target);
        if(target == "Fertilizer"){
            if(VariableManager.Instance.fertilizerAmountInRoom > 0){
                VariableManager.Instance.inputFertilizerTry = true;
            }else{
                VariableManager.Instance.power++;
                StartCoroutine(LackOfFertilizer(3f));
            }
        }else if(target == "FertilizerMaker"){
            VariableManager.Instance.makeFertilizerTry = true;
        }else if(target == "Water"){
            VariableManager.Instance.purifierTry = true;
        }else if(target == "FertilizerResearch"){
            VariableManager.Instance.fertilizerUpgradeTry = true;
        }else if(target == "CO2Fix"){
            VariableManager.Instance.carbonCaptureTry = true;
        }else if(target == "Taurine"){
            VariableManager.Instance.taurineFilterTry = true;
            VariableManager.Instance.hasTaurine = 1;
        }else if(target == "CentralPark"){
            VariableManager.Instance.farmTry = true;
        }else if(target == "Weapon"){
            VariableManager.Instance.asteroidDestroyTry = true;
        }else if(target == "Barrier"){
            VariableManager.Instance.barrierDevTry = true;
        }else if(target == "Observer"){
            VariableManager.Instance.observerTry = true;
            if(VariableManager.Instance.asteroidStatus){
                weapon.GetComponent<TouchEvent>().condition = false;
                int weaponNumOfChild = weapon.transform.childCount;
                for (int i = 0; i < weaponNumOfChild; i++){
                    Debug.Log(weapon.transform.GetChild(i));
                    Debug.Log(weapon.transform.GetChild(i).name);
                    weapon.transform.GetChild(i).gameObject.GetComponent<cakeslice.Outline>().enabled = true;
                    }
                


                }
            }
        VariableManager.Instance.power--;
        powerBar.transform.DOScaleX(0.335f*VariableManager.Instance.power, 1.5f);
        
        if(VariableManager.Instance.power == VariableManager.Instance.workLimit){
            powerBar.GetComponent<UnityEngine.UI.Image>().DOColor(Color.black, 2.0f);
            Color c = mainCamera.GetComponent<cakeslice.OutlineEffect>().lineColor0;
            c.r = 1;
            c.g = 0;
            mainCamera.GetComponent<cakeslice.OutlineEffect>().lineColor0 = c;
        }
        if(VariableManager.Instance.power == 0){
            mainCamera.GetComponent<cakeslice.OutlineAnimation>().enabled = false;
            mainCamera.GetComponent<cakeslice.OutlineEffect>().enabled = false;
            
        }
        int numOfChild = Target.transform.childCount;
        for (int i = 0; i < numOfChild; i++){
            Debug.Log(Target.transform.GetChild(i));
            Debug.Log(Target.transform.GetChild(i).name);
            Target.transform.GetChild(i).gameObject.GetComponent<cakeslice.Outline>().enabled = false;
        }

        Target.gameObject.GetComponent<TouchEvent>().condition = true;
        Debug.Log("Target status: "+ Target.gameObject.GetComponent<TouchEvent>().condition);
        
        if(target == "Observer"){
            if(VariableManager.Instance.asteroidStatus==true){
                Debug.Log("이벤트 코드: "+VariableManager.Instance.eventCode);
                Debug.Log(VariableManager.Instance.asteroidStatus);
                RenderUIPanel("Asteroids");
            }else if(VariableManager.Instance.blackHoleObserved==true){
                RenderUIPanel("BlackHole");
            }else{
                RenderUIPanel("SilentSpace");
            }
        }else{
            interactiveUIPanel.SetActive(false);
            joystick.SetActive(true);
            joystickVision.SetActive(true);
            }

    }
    public void RenderUIPanel(string objectName){
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
        canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("OK").GetComponent<OKButtonDisabler>().Activate();
        canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("Yes").GetComponent<OKButtonDisabler>().Deactivate();
        canvas.transform.Find("InteractiveUIPanel").transform.Find("InteractiveCameraImage").transform.Find("InteractiveUI").transform.Find("No").GetComponent<OKButtonDisabler>().Deactivate();
    }
}

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class YesButton : MonoBehaviour, IPointerClickHandler
{

    // Start is called before the first frame update
    GameObject joystick; 
    GameObject joystickVision;
    GameObject interactiveUIPanel;
    string target;
    GameObject Target;
    GameObject canvas;
    void Start()
    {   
        canvas = GameObject.Find("Canvas");
        joystick = canvas.transform.Find("Analog").gameObject;
        joystickVision = canvas.transform.Find("AnalogVision").gameObject;

    }
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("Yes");
        interactiveUIPanel = GameObject.Find("InteractiveUIPanel");
        target = interactiveUIPanel.transform.GetChild(0).transform.GetChild(0).gameObject.GetComponent<RawImage>().texture.name;
        Debug.Log(target);
        Target = GameObject.Find(target);
        if(target == "Fertilizer"){
            VariableManager.Instance.inputFertilizerTry = true;
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
        }else if(target == "CentralPark"){
            VariableManager.Instance.farmTry = true;
        }else if(target == "Weapon"){
            VariableManager.Instance.asteroidDestroyTry = true;
        }else if(target == "Barrier"){
            VariableManager.Instance.barrierDevTry = true;
        }else if(target == "Observer"){
            VariableManager.Instance.observerTry = true;
        }
        VariableManager.Instance.power--;
        Target.gameObject.GetComponent<TouchEvent>().condition = true;
        Debug.Log("Target status: "+ Target.gameObject.GetComponent<TouchEvent>().condition);
        
        
        interactiveUIPanel.SetActive(false);
        joystick.SetActive(true);
        joystickVision.SetActive(true);
    }
}

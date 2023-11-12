using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SmokeController : MonoBehaviour
{
    string target;
    // Start is called before the first frame update
    void Start()
    {
        target = transform.parent.name;
        if(VariableManager.Instance.purifierStatus == false && target == "Water"){
            gameObject.SetActive(true);
        }else if(VariableManager.Instance.carbonCaptureStatus == 2 && target == "CO2Fix" && gameObject.name == "Smoke2"){
            gameObject.SetActive(true);
        }else if(VariableManager.Instance.carbonCaptureStatus == 1 && target == "CO2Fix"){
            gameObject.SetActive(true);
        }else if(VariableManager.Instance.farmStatus == false && target == "CentralPark"){
            gameObject.SetActive(true);
        }else{
            gameObject.SetActive(false);
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}

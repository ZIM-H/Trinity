using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightController : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if(VariableManager.Instance.monsterDate == VariableManager.Instance.date){
        for(int i = 0; i < transform.childCount; i++){
            transform.GetChild(i).GetComponent<Light>().intensity = 0;
        }
        }


    }

    // Update is called once per frame
    void Update()
    {
        
    }
}

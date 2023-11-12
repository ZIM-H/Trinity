using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LanternController : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if(VariableManager.Instance.monsterDate == VariableManager.Instance.date){
            StartCoroutine(LanternFlashing());
        }else{
            gameObject.SetActive(false);
        }
    }

    IEnumerator LanternFlashing(){
        for(int i = 0; i < 10; i++){
            GetComponent<Light>().intensity = 2;
            yield return new WaitForSeconds(6.0f);
            GetComponent<Light>().intensity = 0;
            yield return new WaitForSeconds(0.5f);
            GetComponent<Light>().intensity = 1;
            yield return new WaitForSeconds(0.1f);
            GetComponent<Light>().intensity = 0;
            yield return new WaitForSeconds(0.1f);
        }
    }




    // Update is called once per frame
    void Update()
    {
        
    }
}

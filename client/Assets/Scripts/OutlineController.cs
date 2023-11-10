using System.Collections;
using System.Collections.Generic;
using cakeslice;
using UnityEngine;

public class OutlineController : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if(VariableManager.Instance.power == 0){
            GetComponent<cakeslice.OutlineEffect>().enabled = false;
            GetComponent<cakeslice.OutlineAnimation>().enabled = false;
        }
        
    }

    // Update is called once per frame
    void Update()
    {
    }
}

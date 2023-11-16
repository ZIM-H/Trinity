using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OKButtonDisabler : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if(gameObject.name == "OK"){
        Deactivate();
        }
    }

    public void Activate(){
        gameObject.SetActive(true);
    }
    public void Deactivate(){
        gameObject.SetActive(false);
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}

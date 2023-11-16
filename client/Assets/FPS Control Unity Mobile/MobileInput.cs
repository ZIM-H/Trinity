using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class MobileInput : MonoBehaviour
{
    public JoyStick joyStick;
    public JoyStick joyStickVision;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        var fps = GetComponent<CharacterControl>();
        fps.RunAxis = joyStick.InputDirection;
        fps.LookAxis = joyStickVision.InputDirection;

    }
}

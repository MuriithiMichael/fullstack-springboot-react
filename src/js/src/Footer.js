import { Button } from 'antd'
import Avatar from 'antd/lib/avatar/avatar'
import React from 'react'
import Container from './Container'
import './Footer.css'

const Footer = (props)=>(
    <div className='footer'> 
        <Container>
            {props.numberOfStudents !== undefined ? 
                <Avatar 
                    size="large"
                    style={{backgroundColor: '#f56a00', marginRight: '5px'}}>
                        {props.numberOfStudents}
                </Avatar> : null
            }
            <Button  onClick={()=>props.handleAddStudentClickEvent()} type="primary">Add new student +</Button>
        </Container>
    </div>
)

export default Footer
import React, { Component } from 'react'
import './App.css';
import { getAllStudents } from './client'
import { errorNotification } from './Notification'
import {
  Table,
  Avatar,
  Spin,
  Modal,
  Empty
} from 'antd'
import { LoadingOutlined } from '@ant-design/icons'
import Container from './Container'
import Footer from './Footer'
import AddStudentForm from './Forms/AddStudentForm'

const getIndicatorIcon = () => <LoadingOutlined type="loading" style={{ fontSize: 24 }}/>
const emptyStyle = {marginTop: '10px'}

class App extends Component {

  state = {
    students: [],
    isFetching: false,
    isAddStudentModalVisible: false
  }

  componentDidMount () {
    this.fetchStudents()
  }

openAddStudentModal = () => this.setState({isAddStudentModalVisible: true})

closeAddStudentModal = () => this.setState({isAddStudentModalVisible: false})

fetchStudents = () =>{
  this.setState({
    isFetching: true
  })
  getAllStudents()
    .then(res => res.json())
    .then(students =>{
      this.setState({
        students,
        isFetching: false
      })
    }).catch(error => {
      console.log(error.error)
      const message = error.error.message
      //const description = error.error.error  
      errorNotification(message, message)
      this.setState({
        isFetching: false
      })
    })
}

  render(){
    const { students, isFetching, isAddStudentModalVisible} = this.state
    
    const commonElements = () =>(
      <div>
        <Modal 
          title='Add new student'
          visible={isAddStudentModalVisible}
          onOk={this.closeAddStudentModal}
          onCancel={this.closeAddStudentModal}
          width={1000}
        >
            <AddStudentForm 
              onSuccess={ () => {
                this.closeAddStudentModal()
                this.fetchStudents()
              }}
              onFailure={ err => {
                const message = err.error.message
                const description = err.error.httpStatus
                errorNotification(message, description)
              }}
            />
        </Modal>
        <Footer 
          numberOfStudents={students.length} 
          handleAddStudentClickEvent={this.openAddStudentModal}
            />
      </div>
    )

    if(isFetching){
      return(
        <Container>
          <Spin indicator={getIndicatorIcon()}/>
        </Container>
      )
    }

    if(students && students.length){
      const columns = [
        {
          title: '',
          key: 'avatar',
          render: (text, student) =>(
            <Avatar size='large'>
              {`${student.firstName.charAt(0).toUpperCase()}${student.firstName.charAt(0).toUpperCase()}`}
            </Avatar>
          )
        },
        {
          title: 'Student Id',
          dataIndex: 'studentId',
          key: 'studentId'
        },
        {
          title: 'First Name',
          dataIndex: 'firstName',
          key: 'firstName'
        },
        {
          title: 'Last Name',
          dataIndex: 'lastName',
          key: 'lastName'
        },
        {
          title: 'Email',
          dataIndex: 'email',
          key: 'email'
        },
        {
          title: 'Gender',
          dataIndex: 'gender',
          key: 'gender'
        }
      ]


      return(
        <Container>
           <Table
            style={{marginBottom: '100px'}}
            dataSource={students} 
            columns={columns} 
            rowKey='studentId'
            pagination={false}/>
            {commonElements()}
        </Container>
       
      )
    }
  
    return (  
      <Container>
        <Empty style={emptyStyle} description={
          <span>No Students Found</span>
        } />
        {commonElements()}
      </Container>
    )
  }

  
}

export default App;

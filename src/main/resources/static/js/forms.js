let fields = []

function attachListeners() {
    [...document.querySelectorAll("a")]
        .forEach(a => a.addEventListener("click", (e) => {
            e.preventDefault();
            const href = e.target.href
            const taskId = e.target.attributes['data'].value
            renderForm(href, taskId).then(r => {
                console.info(r);
                document.getElementById("form_display").innerHTML = r
            })
        }))
}

function flowableTypeToFormAdapter(f) {
    let type = "text"
    switch (f.type) {
        case 'integer':
            type = "number"
            break
    }
    return type;
}

async function renderForm(formLink, taskId) {
    const res = await fetch(formLink)
    const json = await res.json()
    fields = json.fields
    const formId = json.id
    console.info('fields', fields)
    console.info('formId', formId)
    const formFields = fields.map(f => {
        let type = flowableTypeToFormAdapter(f);
        const value = f.value ?? ""
        return `
<div class="row">
    <div class="three columns">${f.name}</div>
    <div class="nine columns"><input class="u-full-width" id="${f.id}" name="${f.name}" type="${type}" value="${value}"/></div>
</div>`
    }).join("\n")
    return `
<form id="submissionForm">
    ${formFields}
    <div class="row">
        <input type="hidden" name="formDefinitionId" id="formDefinitionId" value="${formId}" />
        <input type="hidden" name="taskId" id="taskId" value="${taskId}" />
        <div class="eleven columns"><input type="submit" class="u-full-width" onclick="return submitForm()"></div>
    </div>
</form>
    `
}

function generateUrlAndFormData() {
    const submissionForm = document.getElementById('submissionForm')
    const inputs = [...submissionForm.querySelectorAll("input")]
    const variables = inputs.filter(f => !["submit", "hidden"].includes(f.type)).map(f => {
        const found = fields.find(field => field.id === f.id)
        console.info('found', JSON.stringify(found))
        if (!!found) {
            let value = f.value
            let type = found.type
            if (type === 'text') {
                type = 'string'
            } else if (type === 'integer') {
                value = parseInt(value)
            }
            return {
                "id": found.id,
                "name": found.id,
                "value": value,
                "type": type
            }
        } else {
            return null
        }
    }).filter(e => e !== null)
    const formRequest = {
        "action": "complete",
        "formDefinitionId": document.getElementById("formDefinitionId").value,
        "variables": variables
    }
    const taskId = document.getElementById("taskId").value
    const taskUrl = `/flowable/process-api/runtime/tasks/${taskId}`
    return {formRequest, taskUrl};
}

function writeMessage(message, state) {
    document.getElementById("formMessage").innerHTML = `
<div class="alert alert-${state}">${message}</div>
`
}

function submitForm() {
    const {formRequest, taskUrl} = generateUrlAndFormData();

    console.debug('formRequest', formRequest)
    console.debug('taskUrl', taskUrl)

    fetch(taskUrl, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formRequest)
    }).then(result => {
        if(result.status === 200) {
            alert("Form was successfully submitted. Press OK to refresh")
            window.location.reload()
        } else {
            writeMessage(`Could not submit form successfully: ${result.status}` , "error")
        }
    }).catch(e => {
        console.error(e)
        writeMessage(`Could not submit form successfully: ${e.message}` , "error")
    });


    return false;
}
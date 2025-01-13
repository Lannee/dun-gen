import PropTypes from 'prop-types';

import { Button } from 'primereact/button';

export default function ButtonBlock({handleThrowClick, handleAnotherAttemptClick}) {
    return (
        <div className="card mb-3 mt-2">
            <Button className="mx-3 my-1" label="Throw An Axe" icon="pi pi-check" iconPos="right" onClick={handleThrowClick} />
            <Button className="mx-3 my-1" label="Start Another Attempt" icon="pi pi-refresh" iconPos="right" onClick={handleAnotherAttemptClick} />
        </div>
    );
}

ButtonBlock.propTypes = {
    handleThrowClick: PropTypes.func.isRequired,
    handleAnotherAttemptClick: PropTypes.func.isRequired,
};

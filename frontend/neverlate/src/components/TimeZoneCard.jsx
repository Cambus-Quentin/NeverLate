import { AiFillPushpin, AiFillDelete } from "react-icons/ai";
import { Link } from "react-router-dom";

const TimeZoneCard = ({
  timezone,
  currentTimes,
  displayActionButton,
  handleDelete,
  handlePin,
  classes,
  isPinned,
}) => {
  return (
    <Link
      to={`/edit/${timezone.id}`}
      className={`block p-4 mt-4 border-4 border-black dark:border-white border-double ${classes.bgInvertClass} ${classes.textInvertClass}`}
    >
      <div className="flex flex-row justify-between items-center leading-none">
        <div className="flex flex-col sm:flex-row w-full">

          <div className="w-full sm:w-auto sm:flex-grow mb-2 sm:mb-0 self-end">
            <p
              className="text-xl sm:text-2xl font-semibold break-words text-left"
              style={{ wordWrap: "break-word", width: "135px" }}
            >
              {timezone.label}
            </p>
          </div>


          <div className="flex w-full justify-between sm:w-auto sm:flex-row items-end">

            <div className="text-center self-end">
              <p
                className="text-4xl sm:text-6xl font-bold text-left sm:w-[300px] w-44"
              >
                {currentTimes[timezone.id]}
              </p>
            </div>

            <div className=" text-center sm:self-end">
              <p className="text-md sm:text-xl min-w-20">{`${timezone.offset >= 0 ? "+" : ""
                }${timezone.offset}`}</p>
            </div>
          </div>
        </div>


        {displayActionButton ?
          (<div className="flex flex-col items-center">
            <button
              onClick={(e) => {
                e.preventDefault();
                handlePin(timezone.id);
              }}
              className={`p-2 hover:text-yellow-500 transition duration-300 ease-in-out ${isPinned ? "text-yellow-500" : "inherit"
                }`}
            >
              <AiFillPushpin className="text-2xl" />
            </button>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleDelete(timezone.id);
              }}
              className="p-2 text-red-500 hover:text-red-700 transition duration-300 ease-in-out"
            >
              <AiFillDelete className="text-2xl" />
            </button>
          </div>)
          : (<></>)
        }
      </div>
    </Link>
  );
};

export default TimeZoneCard;
